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
            registrationError: ''
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
                        if (this.email.includes('@mindhubbrothers.com')) {
                           window.location.href = '/web/manager.html';
                        } else {
                            window.location.href = '/web/dashboard.html';
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
                            if (this.email.includes('@mindhubbrothers.com')) {
                                window.location.href = '/web/manager.html';
                             } else {
                                 window.location.href = '/web/dashboard.html';
                             }
                        }
                    })
                    .catch(loginError => {
                        
                        console.error('Login after registration failed:', loginError);
                    });
                })
                .catch(error => {

                    if (error.response && error.response.status === 403) {
                        
                        this.registrationError = 'This email is already registered.';
                        console.error('email repeted');
                    } else {
                        console.error('Registration failed:', error);
                    }
                });
        }
    }  


}


const app = createApp(options)

app.mount('#app')