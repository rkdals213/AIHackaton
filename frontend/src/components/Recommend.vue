<template>
    <v-container>
        <v-item-group mandatory >
            <div class="text-center">
                <v-sheet color="purple lighten-2 rounded-pill" style="color:white;">등록한 카드 기반 추천 결과입니다</v-sheet>
            </div>
            <v-row>
                <v-col
                v-for="(item, index) in type1"
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
        <v-item-group mandatory >
            <div class="text-center">
                <v-sheet color="blue lighten-2 rounded-pill" style="color:white;">금결원 결제 내역 기반 추천 결과입니다</v-sheet>
            </div>
            <v-row>
                <v-col
                v-for="(item, index) in type2"
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
    </v-container>
</template>
<script>
import http from '@/http-common'

export default {
    name: 'Recommend',
    data() {
        return {
            type1: [],
            type2: []
        }
    },
    methods: {
        fetchRecommendCardlist() {
            http.get('/card/RecoCard', {
                params: {
                userId: 'rkdals213'
                }
            })
            .then(({data})=>{
                this.type1 = data.type1
                this.type2 = data.type2
            })
            .catch(err => console.log(err))
        }
    },
    created() {
        this.fetchRecommendCardlist();
    }

}
</script>
