function extractAccountNumber(account) {
    return parseInt(account.substr(3));
}

let { createApp } = Vue
console.log("Accounts")

const options = {
    data() {
        return {
            accounts : [],  
            loans : [],           
            firstName : "",  
            localTime: "",  
            canCreateAccount: false,   
            canApplyForLoan : false,   
            accountType : '',
            selectedLoanToPay: null,
            selectedAccount: null,
        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            console.log(response)            
            this.accounts = response.data.accounts.sort((a, b) => extractAccountNumber(a.number) - extractAccountNumber(b.number));
            console.log(this.accounts)
            this.loans = response.data.loans
            console.log(this.loans)           
            this.firstName =response.data.firstName; 
            console.log(this.firstName)   
            
            this.canCreateAccount = response.data.accounts.length < 3;
            this.canApplyForLoan = response.data.loans.length < 3;
        })
        .catch(error => console.log(error));

        this.updateLocalTime();   
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {

          return `¡Welcome back, ${this.firstName}!`;
        },

        activeAccounts() {
            return this.accounts.filter(account => account.activated);
          }

        
    },

    methods: {  

        formatCurrency(balance) {
            
            return balance.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
        },

        formatDate(date) {
            const formattedDate = new Date(date);
            const day = formattedDate.getDate().toString().padStart(2, "0");
            const month = (formattedDate.getMonth() + 1).toString().padStart(2, "0");
            const year = formattedDate.getFullYear().toString().slice(-2);
            return `${day}/${month}/${year}`;
        },

        updateLocalTime() {
            const now = new Date();
            const hours = now.getHours().toString().padStart(2, "0");
            const minutes = now.getMinutes().toString().padStart(2, "0");
            const seconds = now.getSeconds().toString().padStart(2, "0");
            this.localTime = `${hours}:${minutes}:${seconds}`;
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

        createAccount() {
            const data = `accountType=${(this.accountType)}`
            axios.post('/api/clients/current/accounts', data, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
            })
            .then(response => {
              if (response.status === 201) {
                window.location.reload();
              }
            })
            .catch(error => {
              const resultMessage = error;
              alert(`Error: ${resultMessage}`)
            });
        },

        deactivateAccount(accountId) {
            const data = `id=${(accountId)}`
            axios.patch(`/api/clients/current/accounts/deactivate`, data, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
            })
              .then(response => {
                if (response.status === 200) {
                    window.location.reload();
                }
                console.log(response);
              })
              .catch(error => {
                const resultMessage = error.response.data;
                console.log(error.response.data);
                alert(`Error: ${resultMessage}`)
              });
        },

        confirmDeleteAccount(accountId) {
            if (window.confirm("Are you sure you want to delete this Account?")) {
                this.deactivateAccount(accountId);
            }
        },

        payLoan(){
            const data = `idLoan=${(this.selectedLoanToPay)}&account=${(this.selectedAccount)}`
            console.log(data)
            axios.post(`/api/clients/current/loans/pay-installment`, data, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
            })
              .then(response => {
                if (response.status === 200) {
                    window.location.reload();
                }
                console.log(response);
              })
              .catch(error => {
                const resultMessage = error;
                alert(`Error: ${resultMessage}`)
              });
        }

    }

}


const app = createApp(options)

app.mount('#app')

