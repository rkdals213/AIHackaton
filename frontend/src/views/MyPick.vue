<template>
  <div class="container">
    <v-btn style="margin: 0 auto;" @click="removeItems">선택 카드 제거</v-btn>
    <v-btn style="margin: 0 auto;" @click="postMyPick">선택 카드 저장</v-btn>
    <v-data-table
        v-model="selected"
        :headers="headers"
        :items="cards"
        item-key="name"
        show-select
        class="elevation-1"
    >
    </v-data-table>
    <v-row>
    </v-row>
    <CardsFilter @pluscard="pluscard" />
  </div>
</template>
<script>
import CardsFilter from '@/components/CardsFilter'
import http from '@/http-common'

export default {
    name: 'MyPick',
    components: {
        CardsFilter,
    },
    data () {
      return {
        selected: [],
        headers: [
          {
            text: '카드 명',
            align: 'start',
            sortable: false,
            value: 'name',
          },
          { text: '평점', value: 'score' },
        ],
        cards: []
      }
    },
    methods: {
        removeItems() {
            for(let i=0;i<this.selected.length;i++){
                // console.log(this.selected[i]);
                let itemToFind = this.cards.indexOf(this.selected[i]);
                this.cards.splice(itemToFind,1);
            }
        },
        pluscard(cardInfo) {
            // console.log(cardInfo)
            this.cards.push(cardInfo)
        
        },
        postMyPick() {
          const config = {
            headers: {
              uId: this.$cookies.get('userid')
            }
          }
          const postcards = [];
          for (let i=0; i<this.cards.length;i++){
              let postcard = {};
              postcard.cardid = this.cards[i].no;
              postcard.cardname = this.cards[i].name;
              postcard.rating = this.cards[i].score;
              postcard.userid = this.$cookies.get('userid');
              postcards.push(postcard);
          }
          console.log(config)
          console.log(postcards)
          http.post('user/registCard', postcards, config)
          .then(({data})=>{
              console.log(data);
              // alert('저장완료!')
          })
          .catch(err => console.log(err))
        }
    }
}
</script>