let { createApp } = Vue
console.log("LogIn")

const options = {
    data() {
        return {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            errorMessage: ''
        }
    },

    created() {

    },

    computed: {

    },

    methods: {
        login() {
            const queryParams = `email=${encodeURIComponent(this.email)}&password=${encodeURIComponent(this.password)}`;

            console.log(`/api/login?${queryParams}`);
            axios.post(`/api/login?${queryParams}`)
                .then(response => {
                    if (response.status === 200) {
                        window.location.href = '/web/accounts.html';
                    }
                })
                .catch(error => {
                   this.errorMessage = 'Login failed. Please check your credentials.';
                });
        },

        register() {

            const registrationData = `firstName=${encodeURIComponent(this.firstName)}&lastName=${encodeURIComponent(this.lastName)}
                &email=${encodeURIComponent(this.email)}&password=${encodeURIComponent(this.password)}`;
            
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
                    console.error('Registration failed:', error);
                });
        }
    }  


}


const app = createApp(options)

app.mount('#app')