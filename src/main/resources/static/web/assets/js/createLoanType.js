let { createApp } = Vue

console.log("create loan")

const options = {
    data() {
        return {
            loanName: '',
            maxAmount: 0,
            paymentsInput: '',
            interestRate: 0,
            loans : [],
        }
    },

    created() {
        axios.get("http://localhost:8080/api/loans")
            .then(response => {
                console.log(response)
                this.loans = response.data;
                console.log(this.loans)
            })
            .catch(error => console.log(error));

    },

    methods: {

        createLoanType() {
            const paymentsArray = this.paymentsInput.split(',').map(payment => parseInt(payment.trim(), 10));
            const data ={
                name: this.loanName,
                maxAmount: this.maxAmount,
                payments: paymentsArray,                
                interestRate: this.interestRate,
            }
            axios.post('/api/loans', data)
                .then(response => {                    
                    this.resultMessage = response.data;
                })
                .catch(error => {

                    this.resultMessage = error.response.data;
                    this.errorMessage = '';

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