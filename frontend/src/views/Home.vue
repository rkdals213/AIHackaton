<template>
  <v-container>
    <div class="quickLink">
      <ul class="row text-center">
        
        <div class="col-6 main_button" style="height:200px; width:200px;" @click="to('/About')">
          <img src="@/assets/card.png" style="width:50px;">
          <li >카드등록</li>
        </div>
        <div class="col-6 main_button" style="height:200px; width:200px;" @click="to('/MyInfo')">
          <img src="@/assets/account.png" style="width:50px;">
          <li >내정보</li>
        </div>
      </ul>
      <ul class="row text-center">
        <div class="col-6 main_button" style="height:200px; width:200px;" @click="to('/CardList')">
          <img src="@/assets/list.png" style="width:50px;">
          <li>카드 LIST</li>
        </div>
        <div class="col-6 main_button" style="height:200px; width:200px;" @click="to('/Payed')">
          <img src="@/assets/star.png" style="width:50px;">
          <li >지출내역</li>
        </div>
      </ul>
    </div>
  </v-container>
</template>

<script>

export default {
  name: 'Home',
  data () {
    return {
      
    }
  },
  methods: {
      to(url){
          this.$router.push({path:url});
      }
  }
}
</script>
<style scoped>

.container {
  padding: 0px;
}
.quickLink {
    margin:90px;
    padding-top:40px;
    padding-left:40px;
    padding-right:40px;
    border-radius: 10px;
    border: 1px solid;
    
}
.quickLink ul li a {
    display: block;
    width: 112px;
    padding-top: 58px;
    color: #333;
    line-height: 20px;
    letter-spacing: 0;
}
a, a:link, a:visited, a:active {
    text-decoration: none;
}
a {
    border: none;
}
.main_button {
  cursor: pointer;
  
}

li{
  display: list-item;
  list-style: none!important;
  font-size: 1rem;
  margin-top: 10px;
}
ul  {
   list-style:none!important;
   padding-left:0px;
}
.v-application ul{
  padding-left:0px!important;
}
</style>