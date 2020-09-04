<template>     
  <div class="wrapper" style="margin-top:8%">
    <div class="row">
      <v-container class="col-lg-5 col-sm-6"> 
        <div class="form sm-m-0">
        <v-form
            ref="form"
            v-model="valid"
            lazy-validation
        >
            <v-text-field 
            v-model="id"
            :rules="IdRules"
            :counter="10"
            label="ID*"
            required
            ></v-text-field>
            <v-text-field 
            v-model="password"
            :rules="PwRules"
            :counter="20"
            label="비밀번호*"
            type="password"
            required
            ></v-text-field>
            <v-text-field
            v-model="name"
            :counter="10"
            :rules="nameRules"
            label="이름"
            required
            ></v-text-field>

            <v-select
            v-model="sex"
            :items="items"
            :rules="[v => !!v || 'Item is required']"
            label="성별"
            required
            ></v-select>
            <v-select
            v-model="age"
            :items="age_items"
            :rules="[v => !!v || 'Item is required']"
            label="연령"
            required
            ></v-select>
            <div class="row">
                <div class="col-6">
                    <v-select
                    v-model="circlename"
                    :items="communitylist"
                    :rules="[v => !!v || '모임 종류를 선택하세요']"
                    label="모임 종류"
                    required
                    ></v-select>
                </div>
                <div class="col-6">
                    <v-text-field
                    v-model="circlemember"
                    label="인원 수"
                    required
                    ></v-text-field>
                </div>
            </div>

              <v-btn @click="submit"
              @keyup.enter="submit"
              dark
              large
              tile
              width=100%>
                가입하기
              </v-btn>
              <p class="login-option mt-5 text-center">이미 계정이 있으신가요?
                 <router-link class="login-option-link" :to="{path:'/'}"><b>로그인</b></router-link>
              </p>            
          </v-form>
        </div>
     </v-container>
    </div>
</div>
</template>

<script>
import http from '@/http-common'

  export default {
    name: 'Join', 
    data: () => ({
      valid: true,
      id: '',
      IdRules: [
        v => !!v || '닉네임을 입력해주세요.',
        v => (v && v.length <= 10) || 'Name must be less than 10 characters'
      ],
      password: '',
      PwRules: [
        v => !!v || '비밀번호를 입력해주세요.',
        v => (v && v.length <= 20) || 'Name must be less than 20 characters',
        value => !!value || 'Required.',
        v => v.length >= 8 || 'Min 8 characters',
      ],
      name: '',
      nameRules: [
        v => !!v || 'Name is required',
        v => (v && v.length <= 10) || 'Name must be less than 10 characters',
      ],
      age_items: [
          10,20,30,40,50,60
      ],
      sex: null,
      card_score: null, //카드점수
      items: [
          '남',
          '여',
      ],
      score: [
          1,2,3,4,5
      ],
      communitylist: [
          '운동', '친목', '영화'
      ],
      checkbox: false,
      card: '',
      circlename: '',
      circlemember: null,
      people: '',
      age: null,
    }),
    methods: {
      validate () {
        this.$refs.form.validate()
      },
      reset () {
        this.$refs.form.reset()
      },
      resetValidation () {
        this.$refs.form.resetValidation()
      },
      submit() {
        let sexno = 0;
        if(this.sex=="남"){
          sexno = 0
        } else {
          sexno = 1
        }
        if(this.age=="10대"){
          this.age=10
        }
        let circleid = 0;
        if(this.circlename==="운동"){
          circleid = 1
        }
        else if(this.circlename==="친목") {
          circleid = 2
        }
        else {
          circleid = 3
        }
        // console.log(this.id, this.password, this.name, this.sex, this.age, this.card, this.card_score, this.circlename, this.circlemember)
        if (this.valid != true) {
            alert("주어진 규칙에 맞춰 작성해주세요.");
        } else{
            http.post('/user/regist', {
              id: this.id,
              password: this.password,
              name: this.name,
              sex: sexno,
              age: this.age,
              circlename: this.circlename, 
              circlemember: this.circlemember,
              circlekind: circleid,
            })
            .then(()=> {
              // console.log(data)
              this.$router.push({ name:'Login'})
            })
            .catch(err => console.log(err))
        }
      }
    },    
}
</script>

<style scoped>
.container {
    padding: 0px 15px 0px 15px;
}
a {
    text-decoration: none;
    color: rgb(0,32,96)!important;
    font-weight: 500;
}
.is-fullwidth {
  width: 100%;
  display: block;
  margin: 5px;
}
.primary-button {
    display: inline-block;
    min-width: 104px;
    background: #fb7800;
    padding: 11px 1.5rem 12px;
    border-radius: 0;
    text-align: center;
    cursor: pointer;
    position: relative;
    background: rgb(0,32,96);
    border: 1px solid rgb(0,32,96);
    color: #fff;
}
.form {
    margin-left: 20px;
    margin-right: 20px;
}
.btn{
    margin-top: 20px; 
    margin-right: 10px;
}
.theme--light.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
    background-color: #f5f5f5;
}
.v-text-field {
    padding-top: 30px;
}
.theme--dark.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
    background-color: rgb(0,32,96);
    border-radius: 10px;
}
</style>