import Vue from 'vue'
import Vuex from 'vuex'
import http from "@/http-common";


Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    cards: [],

  },
  getters:{
    cards(state){
      return state
    }
  },
  mutations: {
    setCards(state, payload){
      state.cards = payload;
    }
  },
  actions: {
    getCards(context, payload) {
      http.get(payload).then(({data}) => {
        context.commit("setCards", data);
      })
    }
  },
  modules: {
  }
})
