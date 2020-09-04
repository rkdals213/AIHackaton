<template>
  <v-app>
    <Sidebar />
    <v-main>
      <router-view @login-submit="login" />
    </v-main>
  </v-app>
</template>

<script>
import Sidebar from "./components/Sidebar";
import http from "@/http-common";

export default {
  name: "App",
  components: {
    Sidebar,
  },
  data() {
    return {
      isLoggedIn: false,
    };
  },
  methods: {
    setCookie(token) {
      this.$cookies.set("auth-token", token);
      this.isLoggedIn = true;
    },
    kftch(loginId) {
      window.location.href = "http://localhost:8080/finokeyo/v1/kftc/" + loginId;
    },
    login(loginData) {
      // console.log(loginData)
      http
        .post("/user/login", loginData)
        .then(({ data }) => {
          // console.log(data)
          http
            .post("/user/getUserInfo", data.id)
            .then(({ data }) => {
              this.$cookies.set("userid", data.user.id);
              // this.kftch(data.user.id);
              if (!data.user.checkedin) {
                this.kftch(data.user.id);
              } else {
                this.$router.push({ name: "Home" });
              }
            })
            .catch((err) => console.log(err));
        })
        .catch((err) => (this.errorMessages = err.response.data));
    },
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  // text-align: center;
  color: #2c3e50;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  height: 100vh;
  width: 100vw;
}

@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");
.wrapper {
  font-family: "Noto Sans KR", sans-serif;
  padding: 20px;
}

body {
  font-family: "Noto Sans KR", sans-serif;
}

h2 {
  font-weight: 800;
  font-size: 2.7em;
  border-bottom: 2px solid;
  display: inline;
}
</style>
