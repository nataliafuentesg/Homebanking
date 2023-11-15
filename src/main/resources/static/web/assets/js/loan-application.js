let { createApp } = Vue
console.log("loans")

const options = {
    data() {
        return {

            selectedLoanType: null,
            installments: 0,
            loanAmount: 0,
            selectedDestinationAccount: null,
            selectedLoanInterestRate: null,
            loanTypes: [],
            accounts: [],
            resultMessage: '',
            firstName: '',
            loans: [],
            canApplyForLoan : false,
            showModal: false,


        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.firstName = response.data.firstName;
                console.log(this.firstName)
                this.accounts = response.data.accounts
                console.log(this.accounts)
                this.loans = response.data.loans
                console.log(this.loans)
                this.canApplyForLoan = response.data.loans.length < 3;

            })
            .catch(error => console.log(error));

        axios.get("/api/loans")
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

        selectedLoanInterest() {
            if (this.selectedLoanType) {
                const selectedLoan = this.loanTypes.find(loan => loan.id === this.selectedLoanType);
                if (selectedLoan && selectedLoan.interestRate) {
                    return selectedLoan.interestRate;
                }
            }

            return 0.0; 
        },

        estimatedMonthlyPayment() {
            console.log(this.selectedLoanType)
            console.log('Calculating estimated monthly payment...');
            if (this.selectedLoanType && this.installments && this.loanAmount) {
                const toPay = (((this.loanAmount * this.selectedLoanInterest)/100) + this.loanAmount)/this.installments;                
                return toPay;
            } else {
                return 0.0;
            }
        },

        activeAccounts() {
            return this.accounts.filter(account => account.activated);
          }

       

    },

    methods: {

        formatCurrency(balance) {

            return balance.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
        },

        openLoanModal() {
            this.showModal = true;
          },
          closeLoanModal() {
            this.showModal = false;
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
                interestRate: this.selectedLoanInterest,
                installments: this.installments,
                destinationAccountNumber: this.selectedDestinationAccount,
            }
            console.log(loanData)

            axios.post('/api/clients/current/loans', loanData)
                .then(response => {
                    window.location.href = '/web/accounts.html';
                    this.resultMessage = response.data;
                })
                .catch(error => {

                    this.resultMessage = error.response.data;
                    this.errorMessage = '';

                });
        },

        getInstallments(selectedLoanType) {
            const loanType = this.loanTypes.find(loan => loan.id === selectedLoanType);
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
        },

    }


}


const app = createApp(options)

app.mount('#app')