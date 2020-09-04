from flask import Flask, request, jsonify, render_template
import numpy as np
import pandas as pd
import dbModule
import math, sys
from mlxtend.preprocessing import TransactionEncoder
from mlxtend.frequent_patterns import apriori, association_rules
import sklearn.externals
import joblib
from sklearn.decomposition import TruncatedSVD

import json

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
# api = Api(app)
db_class = dbModule.Database()

################## <Type 2 : 계좌정보 기반으로 추천> ###########################
@app.route('/kftcAI', methods = ['POST'])
def accountInfo():
    value = request.form['list']
    print(value)
    value = json.loads(value)
    len_dataset = len(value)
    tmp_dataset = {}
    # print(value[0]["tran_date"])
    for i in range(len_dataset):

        t = value[i]["tran_date"]
        if value[i]["tran_date"] in tmp_dataset:
            tmp_dataset[t] += [value[i]["branch_name"]]
        else:
            tmp_dataset[t] = [value[i]["branch_name"]]

    dataset = []
    for item in tmp_dataset.values():
        dataset.append(item)

    te = TransactionEncoder()
    te_ary = te.fit(dataset).transform(dataset)
    df = pd.DataFrame(te_ary, columns=te.columns_)
    frequent_itemsets = apriori(df, min_support=0.5, use_colnames=True)

    testtmp = association_rules(frequent_itemsets, metric="lift", min_threshold=1)

    testtmp.sort_values('lift', ascending=False, inplace=True)

    recTemp1 = list(testtmp.iloc[0, 0])
    recTemp2 = list(testtmp.iloc[0, 1])

    word1 = recTemp1[0]
    word2 = recTemp2[0]
    sql = """select no, name 
          from card 
          where benefits like %s or %s"""
    result = db_class.executeAll(sql, (('%' + word1 + '%', '%' + word2 + '%')))
    result = pd.DataFrame(result)
    result = result.to_json(force_ascii=False)

    print(result)
    return result

@app.route('/users/<loginUser>')
def user_info(loginUser):
    ################### <Type 1 : 유저 카드 평점을 기반으로 추천> ###########################
    rec_type = 1
    sql = "SELECT * FROM usercard"
    usercard = db_class.executeAll(sql)
    usercard = pd.DataFrame(usercard)
    usercard_len = usercard.iloc[-1,0] # db 마지막 길이
    usercard=usercard.iloc[:,[1,2,4]]
    usercard.columns = ['userid','cardid','rating']
    # ratings = ratings[['userid','cardid','rating']]
    usercard_rating= pd.pivot_table(usercard, values='rating', index=['userid'], columns=['cardid'])

    row_mean= list(usercard_rating.mean(axis=1))
    col_mean= list(usercard_rating.mean())

    # k = 현재 인원수에 비례해서해야한다.
    k = 0
    if usercard_len <= 10 :
        k = usercard_len//2
    elif usercard_len < 50 :
        k = usercard_len//5
    else :
        k = 10

    def R_filled_in(usercard_rating, col_mean):
        result= usercard_rating
        for col in range(len(result.columns)):
            col_values= list(result.iloc[:,col])
            result.iloc[:,col]=[i if math.isnan(i) == False else col_mean[col] for i in col_values]
            
        return result

    R_filled_in = R_filled_in(usercard_rating, col_mean)

    def R_norm(R_filled_in, row_mean):
        result = R_filled_in
        for i in range(len(row_mean)):
            result.iloc[i,:] = [v-row_mean[i] for v in result.iloc[i,:]]

        return result

    R_norm = R_norm(R_filled_in, row_mean)

    # k : 행렬 수(요인) n :  계산반복횟수  높은 k를 선택하면 R에 가까운 근사치를 얻을 수 있다. 반면에, 더 작은 k를 선택하면 더 많은 일을 절약할 수 있을 것
    def SVD(R_norm, k, n = 5): 
        svd = TruncatedSVD(n_components=k, n_iter=n)
        svd.fit(np.array(R_norm))
        U=svd.fit_transform(np.array(R_norm))
        Sigma=svd.explained_variance_ratio_
        VT= svd.components_

        ratings_reduced= pd.DataFrame(np.matmul(np.matmul(U, np.diag(Sigma)), VT)) 

        # 유사도를 구하기 위한 행렬
        Sigma_sqrt=[]
        for i in range(len(Sigma)):
            tmp=[]
            tmp=[math.sqrt(s) for s in np.diag(Sigma)[i]]
            Sigma_sqrt.append(tmp)

        # m 아이템 행렬 생성
        sqrt_Sigma_mul_VT = pd.DataFrame(np.matmul(Sigma_sqrt, VT))

        return ratings_reduced, sqrt_Sigma_mul_VT

    R_reduced, sqrt_S_VT=SVD(R_norm, k)

    def cos_sim(x,y):
        
        x_2 = math.sqrt(sum([pow(i,2) for i in x]))
        y_2 = math.sqrt(sum([pow(i,2) for i in y]))
        multi= sum([i*j for i,j in zip(x, y)])
        
        return multi / (x_2 * y_2)
    pred_matrix = []

    for a in range(len(R_reduced.index)):
        K = list(usercard[usercard['userid']==a+1]['cardid'])
        a_j_pred=[]
        
        for j in range(len(R_reduced.columns)): 
            sim_basket = []
            
            for k in K:
                j_k_sim = cos_sim(sqrt_S_VT.iloc[:,j], sqrt_S_VT.iloc[:,k-1])
                sim_basket.append([k, j_k_sim])    
            
            top_k= pd.DataFrame(sim_basket,columns=['k','similarity']).sort_values('similarity',ascending=False).head(5).reset_index().iloc[1:,:] # head(11)
            
            sum_sim = sum([np.abs(i) for i in list(top_k.loc[:,'similarity'])])

            # !division by zero
            if sum_sim == 0:
                sum_sim = sys.float_info.epsilon

            sum_up = []
            for kk in list(top_k.loc[:,'k']):
                sum_up.append(float(top_k[top_k['k']==kk]['similarity'] * (R_reduced.iloc[a,j-1] + row_mean[a-1])))
            
            a_j_pred.append((sum(sum_up)/sum_sim))
        pred_matrix.append(a_j_pred)

    pred = pd.DataFrame(pred_matrix)
    # 필요없는 인덱스 제거
    pred.drop([pred.columns[0]],axis=1, inplace=True)

    sql = "SELECT * FROM card"
    card_info = db_class.executeAll(sql)
    card_info = pd.DataFrame(card_info)

    card_info=card_info.iloc[:,0:3]
    card_info.columns = ['cardid', 'name', 'company']
    card_info.head()

    def recommand(user, top, pred, card_info):
        p = pred
        c = card_info
        
        tmp = pd.DataFrame(p.loc[user,:].T)
        tmp['cardid']= tmp.index
        tmp.columns=['predict','cardid']
        tmp.sort_values('predict', ascending=False, inplace=True)
        tmp=pd.DataFrame(tmp.head(5))

        tmp=pd.merge(tmp, c, on='cardid')
        return tmp

    # 여기 1을 바꿔야한다. 해당 userid로
    print(loginUser)
    r = recommand(int(loginUser), 5, pred, card_info) # userid, 몇개 뽑을지
    r = r.to_json()
    return r 

if __name__ == "__main__":
    app.run(debug=True)
    # app.run(host='0.0.0.0', port=8080) // 어떤 호스트에서도 연결 가능하도록
