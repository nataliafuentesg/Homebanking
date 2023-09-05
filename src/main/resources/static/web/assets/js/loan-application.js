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
            //estimatedMonthlyPayment: 0.0,
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
                // Assuming interest rate is 20% divided by 12 (for monthly payments)
                const interestRate = 0.20 / this.installments;
                const numberOfPayments = this.installments;
                const principal = this.loanAmount;

                // Formula to calculate monthly payment
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
                    console.log('Loan application submitted successfully:', response.data);                    
                })
                .catch(error => {
                   
                    console.error('Error submitting loan application:', error);
                    
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