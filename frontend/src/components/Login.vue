<template>
<div class="wrapper" style="margin-top:8%">
    <div class="row">
        <v-container class="col-lg-5 col-sm-6">
        <div class="form sm-m-0">
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field 
            v-model="memberId"
            :rules="IdRules"
            label="ID*"
            required
            ></v-text-field>
            <div>
              <v-text-field 
              v-model="memberPw"
              :rules="PwRules"
              :counter="20"
              label="비밀번호*"
              required
              >
              </v-text-field>
            </div>
            <br>
            <footer class="login-foot">
                <v-btn @click="submit" dark large tile width=100%>로그인</v-btn>
                <div class="login-divider" role="separator"></div>
                <p class="login-option mt-5 text-center">FinOkeYo 처음이신가요?
                <router-link class="login-option-link" :to="{path:'/join'}">가입하기</router-link>
                </p>
            </footer>
          </v-form>
        </div>
        </v-container>
    </div>
</div>
</template>

<script>
export default {
    name: 'Login',
    data: () => ({
      valid: true,
      memberId: '',
      IdRules: [
        v => !!v || 'ID를 입력해주세요.',
        v => (v && v.length <= 10) || 'ID must be less than 10 characters'
      ],
      memberPw: '',
      PwRules: [
        v => !!v || '비밀번호를 입력해주세요.',
        v => (v && v.length <= 20) || 'PW must be less than 20 characters'
      ],
    }),

    methods: {
      submit () {
        // console.log(this.memberId, this.memberPw)
        this.$emit('login-submit',{
          id: this.memberId,
          password: this.memberPw,
        })        
      },
      showPw (){
      }
    }
}
</script>

<style scoped>
.container {
    padding: 0px 15px 0px 15px;
}
a {
    text-decoration: none;
    color: rgb(0,32,96)!important;
    font-weight: 500;
}

.is-fullwidth {
  width: 100%;
  display: block;
  margin: 5px;
}
.primary-button {
    display: inline-block;
    min-width: 104px;
    background: #fb7800;
    padding: 11px 1.5rem 12px;
    border-radius: 0;
    text-align: center;
    cursor: pointer;
    position: relative;
    background: #161616;
    border: 1px solid #161616;
    color: #fff;
}
.form {
    margin-left: 20px;
    margin-right: 20px;
}
.btn{
    margin-top: 20px; 
    margin-right: 10px;
}
.theme--light.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
    background-color: #f5f5f5;
}
.v-text-field {
    padding-top: 30px;
}
#l {
	background-color: rgba(0,0,0,0.7);
	border-radius: 0.75em;
	box-shadow: 0.125em 0.125em 0 0.125em rgba(0,0,0,0.3) inset;
	color: #fdea7b;
	display: inline-flex;
	align-items: center;
	margin: auto;
	padding: 0.15em;
	width: 3em;
	height: 1.5em;
	transition: background-color 0.1s 0.3s ease-out, box-shadow 0.1s 0.3s ease-out;
	-webkit-appearance: none;
	-moz-appearance: none;
	appearance: none;
}
#l:before, #l:after {
	content: "";
	display: block;
}
#l:before {
	background-color: #d7d7d7;
	border-radius: 50%;
	width: 1.2em;
	height: 1.2em;
	transition: background-color 0.1s 0.3s ease-out, transform 0.3s ease-out;
	z-index: 1;
}
#l:after {
	background:
		linear-gradient(transparent 50%, rgba(0,0,0,0.15) 0) 0 50% / 50% 100%,
		repeating-linear-gradient(90deg,#bbb 0,#bbb,#bbb 20%,#999 20%,#999 40%) 0 50% / 50% 100%,
		radial-gradient(circle at 50% 50%,#888 25%, transparent 26%);
	background-repeat: no-repeat;
	border: 0.25em solid transparent;
	border-left: 0.4em solid #d8d8d8;
	border-right: 0 solid transparent;
	transition: border-left-color 0.1s 0.3s ease-out, transform 0.3s ease-out;
	transform: translateX(-22.5%);
	transform-origin: 25% 50%;
	width: 1.2em;
	height: 1em;
}
#l:checked {
	background-color: rgba(0,0,0,0.45);
	box-shadow: 0.125em 0.125em 0 0.125em rgba(0,0,0,0.1) inset;
}
#l:checked:before {
	background-color: currentColor;
	transform: translateX(125%)
}
#l:checked:after {
	border-left-color: currentColor;
	transform: translateX(-2.5%) rotateY(180deg);
}
#l:focus {
	outline: 0;
}


.theme--dark.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
    background-color: rgb(0,32,96);
    border-radius: 10px;
}
</style>