<template>
  <v-col>
    <!-- <v-row> -->
    <v-card v-for="card in cards" :key="card.no" class="mx-auto" max-width="400">
      <v-img class="white--text align-end" :src="card.images">
        <v-card-title>{{ card.name }}</v-card-title>
      </v-img>

      <v-card-subtitle class="pb-0">{{ card.company }}</v-card-subtitle>

      <v-card-text class="text--primary">
        <div>{{ card.benefits }}</div>
      </v-card-text>

      <!-- <v-card-actions>
      <v-btn
        color="orange"
        text
      >
        Share
      </v-btn>

      <v-btn
        color="orange"
        text
      >
        Explore
      </v-btn>
    </v-card-actions> -->
    </v-card>
    <!-- </v-row> -->
  </v-col>
</template>

<script>
import axios from "axios";

const SERVER_URL = "http://localhost:8080/finokeyo";

export default {
  name: "CardList",
  data() {
    return {
      cards: [],
    };
  },
  methods: {
    fetchCardList() {
      axios
        .get(SERVER_URL + "/v1/card/CardAll")
        .then(({ data }) => {
          this.cards = data;
        })
        .catch((err) => console.log(err));
    },
  },
  created() {
    this.fetchCardList();
  },
};
</script>
