let { createApp } = Vue
console.log("transfer")

const options = {
    data() {
        return {
            transferType: 'OWN',
            fromAccountNumber: '',
            toAccountNumber: '',
            amount: 0,
            description: '',
            accounts: [], 
            resultMessage: '',
            firstName :'',


        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients/current")
            .then(response => {
                console.log(response)
                this.firstName = response.data.firstName;
                console.log(this.firstName)
                this.accounts = response.data.accounts
                console.log(this.accounts)

            })
            .catch(error => console.log(error));
        
        this.updateLocalTime();
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {
            return `¡Welcome back, ${this.firstName}!`;
        }


    },

    methods: {


        updateLocalTime() {
            const now = new Date();
            const hours = now.getHours().toString().padStart(2, "0");
            const minutes = now.getMinutes().toString().padStart(2, "0");
            const seconds = now.getSeconds().toString().padStart(2, "0");
            this.localTime = `${hours}:${minutes}:${seconds}`;
        },

        makeTransfer(){
            const transferData = `fromAccountNumber=${(this.fromAccountNumber)}&toAccountNumber=${(this.toAccountNumber)}&amount=${(parseFloat(this.amount))}&description=${(this.description)}`

            console.log(`fromAccountNumber=${(this.fromAccountNumber)}&toAccountNumber=${(this.toAccountNumber)}&amount=${(parseFloat(this.amount))}&description=${(this.description)}`)

            axios.post('/api/clients/current/transactions', transferData,{
                headers: {'content-type': 'application/x-www-form-urlencoded'}
              })
                .then(response => {
                    if (response.status === 201) { 
                        window.location.href = '/web/accounts.html';
                        this.resultMessage = 'Transfer successful.';                        
                    } else {
                        this.resultMessage = 'Transfer failed.';
                    }
                })
                .catch(error => {
                    this.resultMessage = error.response.data ;
                });
        
        },

        logout() {
            axios.post('/api/logout')
                .then(response => {
                    if (response.status === 200) {
                        window.location.href = '/web/assets/pages/index.html';
                    }
                })
                .catch(error => {
                    console.error('Logout failed:', error);
                });
        }

    }


}


const app = createApp(options)

app.mount('#app')