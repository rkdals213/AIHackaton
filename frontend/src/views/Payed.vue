<template>
  
  <v-card>
    <!-- <VuetifyLoader /> -->
    <v-card-title>
      금결원 지출 내역
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="desserts"
      :search="search"
    ></v-data-table>
  </v-card>
</template>


<script>
import http from '@/http-common'

  export default {
    name: 'Payed',
    data () {
      return {
        search: '',
        headers: [
          {
            text: 'tran_date',
            align: 'start',
            sortable: true,
            value: 'tran_date',
          },
          { text: 'Company', value: 'print_content' },
          { text: 'type', value: 'tran_type' },
          { text: 'after_balance_amt', value: 'after_balance_amt' },
          { text: 'branch_name', value: 'branch_name' },
          { text: 'tran_amt', value: 'tran_amt' },
          { text: 'tran_time', value: 'tran_time' },
        ],
        desserts: [],
      }
    },
    created() {
      const config = {
       headers: {
        'access-token': this.$cookies.get('access-token'),
        'use-token': this.$cookies.get('use-token')
        }
      }
      http.get('/kftc/accountList', config)
      .then(({data}) => {
        this.desserts = data;
        // console.log(this.desserts);
      })
      .catch(err => console.log(err))
    }
  }
</script>