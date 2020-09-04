<template>
  <v-row justify="center">
    <v-col cols="12" sm="8">
      <v-card>
        <v-list two-line subheader>
          <v-subheader><h3><v-icon>mdi-account-box-outline</v-icon> 내 정보</h3></v-subheader>
          
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title><v-icon color="yellow darken-2">mdi-check</v-icon> 동호회 명</v-list-item-title>
              <v-list-item-subtitle style="margin-left:10px; ">축구동아리</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-divider style="margin: 0 15px;"></v-divider>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title><v-icon color="yellow darken-2">mdi-check</v-icon> 동호회 종류</v-list-item-title>
              <v-list-item-subtitle style="margin-left:10px; ">축구동아리</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-divider style="margin: 0 15px;"></v-divider>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title><v-icon color="yellow darken-2">mdi-check</v-icon> 성별 </v-list-item-title>
              <v-list-item-subtitle style="margin-left:10px;">Your status is visible to everyone</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider style="margin: 0 15px;"></v-divider>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title><v-icon color="yellow darken-2">mdi-check</v-icon> 연령대 </v-list-item-title>
              <v-list-item-subtitle style="margin-left:10px;">Your status is visible to everyone</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider style="margin: 0 15px;"></v-divider>
        </v-list>
          <v-list-item>
            <v-list-item-content>
               <v-list-item-title><v-icon color="red">mdi-heart</v-icon> 소비패턴 분석 결과 </v-list-item-title>
               
            </v-list-item-content>
          </v-list-item>
          <v-divider style="margin: 0 15px; "></v-divider>
        <div>
           <apexchart type="donut"  height="450" :options="donutOptions" :series="donutSeries" class="tag-list"></apexchart>
        </div>
          <v-list-item>
            <v-list-item-content>
              
              <v-list-item-subtitle style="margin-left:10px;" class="text-center">
                <v-btn>카드 추천받기!</v-btn>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider style="margin: 0 15px;"></v-divider>
      </v-card>
    </v-col>
  </v-row>
</template>


<script>
import http from '@/http-common'
  export default {
    name: 'MyInfo',
    data: function() {
      return {
            donutSeries: [],
            donutOptions: {
            chart: {
                type: 'donut',
                },           
              labels: [],
              plotOptions: {
                  pie: {
                      customScale: 0.9,
                      donut: {
                          size: '50%'
                      }
                  }
            },
            responsive: [{
                breakpoint: 480,
                options: {
                    chart: {
                        width: 200,
                        height: 330
                    },
                  
                    legend: {
                        position: 'bottom'
                        }
                    }
                }]
            },
          
          
          
      }
    
          
        
    },
    created() {
      const config = {
       headers: {
        'access-token': this.$cookies.get('access-token'),
        'use-token': this.$cookies.get('use-token')
        }
      }
      http.get('/kftc/ConsumptionPattern', config)
      .then(({data}) => {
        
        var dataValue = []
        for (var prop in data[0]) {
          this.donutOptions.labels.push(prop)
          dataValue.push(data[0][prop])
        }
        this.donutSeries = dataValue
      
      })
      .catch(err => console.log(err))
    }
  }
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap');

v-card v-sheet theme--light{
  font-family: 'Noto Sans KR', sans-serif;
}
.v-list-item__title, .v-list-item__subtitle{
  font-family: 'Noto Sans KR', sans-serif;
  padding-bottom: 10px;
  color:rgb(0,32,96);
}

h3{
  font-family: 'Noto Sans KR', sans-serif;
}
</style>