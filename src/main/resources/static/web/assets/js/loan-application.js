let { createApp } = Vue
console.log("loans")

const options = {
    data() {
        return {

            selectedLoanType: null,
            installments: 0,
            loanAmount: 0,
            selectedDestinationAccount: null,
            loanTypes: [],
            accounts: [],
            resultMessage: '',
            firstName: '',


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

        axios.get("http://localhost:8080/api/loans")
            .then(response => {
                console.log(response)
                this.loanTypes = response.data;
                console.log(this.loanTypes)
            })
            .catch(error => console.log(error));

        this.updateLocalTime();
        setInterval(this.updateLocalTime, 1000);

              
    },

    computed: {
        welcomeMessage() {
            return `¡Welcome back, ${this.firstName}!`;
        }, 
        
        estimatedMonthlyPayment() {
            console.log('Calculating estimated monthly payment...');
            if (this.selectedLoanType && this.installments && this.loanAmount) {
                const interestRate = 0.20 / this.installments;
                const numberOfPayments = this.installments;
                const principal = this.loanAmount;
                const x = Math.pow(1 + interestRate, numberOfPayments);
                return (principal * x * interestRate) / (x - 1);
            } else {
                return 0.0;
            }
        },

    },

    methods: {

        formatCurrency(balance) {
            
            return balance.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
        },

        updateLocalTime() {
            const now = new Date();
            const hours = now.getHours().toString().padStart(2, "0");
            const minutes = now.getMinutes().toString().padStart(2, "0");
            const seconds = now.getSeconds().toString().padStart(2, "0");
            this.localTime = `${hours}:${minutes}:${seconds}`;
        },        

        submitLoanApplication() {
            
            const loanData = {
                loanId: this.selectedLoanType,
                amount: this.loanAmount,
                installments: this.installments,
                destinationAccountNumber: this.selectedDestinationAccount,
            }
            console.log(loanData)

            axios.post('/api/clients/current/loans', loanData)
                .then(response => {            
                    window.location.href = '/web/accounts.html';        
                    this.resultMessage = response.data ;                   
                })
                .catch(error => {
                   
                    this.resultMessage = error.response.data ;
                    this.errorMessage = '';
                    
                });               
        },      
        
        getInstallments(selectedLoanType) {
            // Find the loan type by its ID
            const loanType = this.loanTypes.find(loan => loan.id === selectedLoanType);
    
            // Return the installments array of the selected loan type
            return loanType ? loanType.payments : [];
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