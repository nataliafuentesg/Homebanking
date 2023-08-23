let { createApp } = Vue
console.log("LogIn")

const options = {
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            errorMessage: '',
            loginError: ''
        }
    },

    created() {

    },

    computed: {

    },

    methods: {
        login() {
            const loginData = `email=${(this.email)}&password=${(this.password)}`;

            console.log(`/api/login?${loginData}`);
            axios.post(`/api/login?${loginData}`)
                .then(response => {
                    if (response.status === 200) {
                        if (this.email === 'admin@mindhubbrothers.com') {
                           window.location.href = '/web/manager.html';
                        } else {
                            window.location.href = '/web/accounts.html';
                        }
                    }
                    
                })
                .catch(error => {
                   this.errorMessage = 'Login failed. Please check your credentials.';
                });
        },

        register() {

            const registrationData = `firstName=${(this.firstName)}&lastName=${(this.lastName)}
                &email=${(this.email)}&password=${(this.password)}`;
            
            console.log(registrationData);

            axios.post('/api/clients', registrationData)
            
                .then(response => {
                    
                    axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                        headers: {'content-type': 'application/x-www-form-urlencoded'}
                    })
                    .then(loginResponse => {
                        if (loginResponse.status === 200) {
                            window.location.href = '/web/accounts.html';
                        }
                    })
                    .catch(loginError => {
                        
                        console.error('Login after registration failed:', loginError);
                    });
                })

                console.log('/api/login', `email=${this.email}&password=${this.password}`)
                .catch(error => {

                    if (error.status === 403) {
                        this.loginError = 'This email its already registered';
                        console.error('Registration failed:', error);
                    }
                    console.error('Registration failed:', error);
                });
        }
    }  


}


const app = createApp(options)

app.mount('#app')