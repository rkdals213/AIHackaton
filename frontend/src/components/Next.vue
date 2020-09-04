<template>
<v-container>
    loading..
</v-container>
</template>
<script>
import http from '@/http-common'

export default {
    name: 'Next',
    methods: {
    },
    created() {
        http.get('/kftc/getToken')
        .then(({data}) => {
            console.log(data);
            this.$cookies.set('access-token', data.token['access-token'])
            this.$cookies.set('use-token', data.token['use-token'])
            this.$router.push({ 'name' : 'Home' });
        })
        .catch(err => console.log(err))
    }
}
</script>
