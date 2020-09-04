<template>
<div class="filter">
  <v-container fluid>
    <v-btn-toggle
      dense
      multiple
    >
    <v-row align="center" justify="space-around">
    <v-btn class="rounded-pill" @click="filterButton('')">
        모두보기
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('신한')">
        신한카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('국민')">
        국민카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('카카오')">
        카카오뱅크카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('BC')">
        BC카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('롯데')">
        롯데카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('삼성')">
        삼성카드
    </v-btn>
    <v-btn class="rounded-pill" @click="filterButton('현대')">
        현대카드
    </v-btn>
    </v-row>
    </v-btn-toggle>
    <v-data-iterator
      :items="items"
      :items-per-page.sync="itemsPerPage"
      :page="page"
      :search="search"
      :sort-by="sortBy.toLowerCase()"
      :sort-desc="sortDesc"
      hide-default-footer
    >

    <template v-slot:default="props">
      <v-item-group mandatory >
          <v-row>
            <v-col
              v-for="(item, index) in props.items"
              :key="index"
              cols="6"
              sm="4"
              md="3"
              lg="3"
            >

            <v-item v-slot:default="{  }" height="250">
              <v-card
                class="mx-auto"
                max-width="400"
                @click="submit(item, index)"
                  
              >
              <v-img
                class="white--text align-end"
                height="100px"
                :src="item.images"
              >
              </v-img>
              <v-card-title style="font-size: 12px; font-weight:bold; padding: 4px 2px; text-align: center;">{{ item.name }}</v-card-title>
              </v-card>
            </v-item>
          </v-col>
        </v-row>
      </v-item-group>
    </template>
    <template v-slot:footer>
        <v-row class="mt-2" align="center" justify="center">
          <!-- <v-spacer></v-spacer> -->
          <!-- <span
            class="mr-4
            grey--text"
          >
          </span> -->
          <v-btn
            fab
            dark
            color="blue darken-3"
            class="mr-1"
            @click="formerPage"
          >
            <v-icon>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn
            fab
            dark
            color="blue darken-3"
            class="ml-1"
            @click="nextPage"
          >
            <v-icon>mdi-chevron-right</v-icon>
          </v-btn>
        </v-row>
      </template>
    </v-data-iterator>
  </v-container>
  <v-dialog v-model="dialog" persistent max-width="500px">
    <v-card>
      <v-card-title class=" text-center headline yellow lighten-2">
        <template>
          <span large v-text="company ">
          </span>
          <span class="ml-2 mr-2"> / </span>
          <span large v-text="name"></span>
        </template>
        <v-spacer></v-spacer>
        <v-btn icon @click="closeDialog()"> <!-- closeDialog 클릭 이벤트 -->
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-title>
     
      </v-card-title>
      <v-spacer></v-spacer>
      <v-card-text class="text-right">
        <v-chip       
        color="orange"
        outlined>
        카드혜택
        </v-chip>
      </v-card-text>
      <v-card-text>
        <div v-text="benefits" class="mt-3 card_content"></div>

      </v-card-text>
      <v-card-text>
        <v-select
          v-model="cardSelect"
          :items="cardItems"
          :rules="[v => !!v || '평점을 선택해주세요']"
          label="카드 평점"
          required
        ></v-select>
      </v-card-text>
      <v-card-text class="text-center">
        <v-btn @click="plusCard">카드 등록</v-btn>
      </v-card-text>
    </v-card>
  </v-dialog>
</div>
</template>

<script>
import http from '@/http-common'
  export default {
    data () {
      return {
        dialog: false,
        itemsPerPageArray: [4, 8, 12],
        search: '',
        filter: {},
        sortDesc: false,
        page: 1,
        itemsPerPage: 10,
        sortBy: 'name',
        keys: [
          'Name',
          'Calories',
        ],
        items: [
        ],
        name: '', //카드이름
        company: '', //카드회사
        benefits: '', //카드혜택
        cardSelect: '', //카드평점
        cardItems: [1,2,3,4,5],
        cardno: 0,
      }
    },
    computed: {
      numberOfPages () {
        return Math.ceil(this.items.length / this.itemsPerPage)
      },
      filteredKeys () {
        return this.keys.filter(key => key !== `Name`)
      },
    },
    methods: {
      nextPage () {
        if (this.page + 1 <= this.numberOfPages) this.page += 1
      },
      formerPage () {
        if (this.page - 1 >= 1) this.page -= 1
      },
      updateItemsPerPage (number) {
        this.itemsPerPage = number
      },
      filterButton (filter) {
          this.search = filter;
      },
      fetchCardList() {
        http.get('/card/CardAll')
        .then(({data}) => {
          this.items = data;
          })
          .catch(err => console.log(err))
      },
      submit(item) {
        this.cardno = item.no
        this.name = item.name
        this.company = item.company
        this.benefits = item.benefits
        this.dialog = true
      },
      closeDialog() {
        this.dialog = false
      },
      plusCard() { //카드등록
        if (this.cardSelect === ""){
          alert('평점을 선택해주세요');
        }
        else {
          this.$emit('pluscard',{
            no : this.cardno,
            name : this.name,
            score : this.cardSelect
          })
          this.cardSelect = "";
          this.closeDialog();
        }
      }

    },
    created() {
      this.fetchCardList();
    },
    mounted() {
    }
  }
</script>

<style scoped>
.filter {
  padding: 15px;
}
.v-card {
  cursor: pointer;

}

.headline span {
  font-size: 1.2rem;
  font-weight: 700;
}

.card_content{
  font-weight: 700;
}
.rounded-pill{
  margin:5px;
}
</style>

