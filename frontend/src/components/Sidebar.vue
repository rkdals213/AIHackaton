<template>
    <div class="wrapper">
      
    <v-toolbar>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>

      <v-toolbar-title class="mt-3" @click="to('/')"><img width="150" src="@/assets/logo.png"/></v-toolbar-title>

      <v-spacer></v-spacer>

      <!-- <v-btn icon @click="to('/join')">
        회원가입
      </v-btn> -->

      <v-btn icon @click="to('/login')">
        <v-icon>mdi-account mdi-36px</v-icon>
        
      </v-btn>
      <!-- <v-btn icon @click="to('/join')">
        내정보
      </v-btn>

      <v-btn icon @click="to('/login')">
        로그아웃
      </v-btn> -->
    </v-toolbar>

    <v-navigation-drawer
        absolute
        v-model="drawer"
        temporary
    >
    <br>
    <v-list>
      <v-list-item-group v-model="model">
        <v-list-item
          v-for="(item, i) in items"
          :key="i"
          @click="to(item.to)"
        >
          <v-list-item-icon>
            <v-icon v-text="item.icon"></v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title v-text="item.text"></v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list-item-group>
    </v-list>
    </v-navigation-drawer>
    </div>
</template>

<script>
export default {
    name: 'Sidebar',
    data: () => ({
        drawer: false,
        items: [
        {
          icon: 'mdi-credit-card ',
          text: '카드등록',
          to: 'MyPick'
        },
        {
          icon: 'mdi-account',
          text: '내정보',
          to: '/MyInfo'
        },        
        {
          icon: 'mdi-star',
          text: '카드 List',
          to: '/CardList'
        },
        {
          icon: 'mdi-format-list-bulleted',
          text: '지출내역',
          to: 'Payed',
        },
        {
          icon: 'mdi-heart',
          text: '카드 추천',
          to: 'Recommend',
        },

      ],
      model: 1,
    }),
    methods: {
        to(url){
            this.$router.push({path:url});
        }
    }
}
</script>

<style scoped>
@media (max-width:750px){
    .logo {
    width: 70%;
    text-align: center;
    cursor: pointer;
    }
}
img{
  cursor: pointer;
}
.emoji {
    size: 30px;
}
h2 {
    font-size: 4rem;
    margin: 30px;
    margin-top: 30px;
    text-decoration: none;
}
.v-sheet.v-toolbar:not(.v-sheet--outlined) {
    box-shadow: none;
}
.v-btn {
    margin:15px;
}
</style>