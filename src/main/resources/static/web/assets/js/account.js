

let { createApp } = Vue
console.log("Account")

const options = {
    data() {
        return {
            account : [], 
            firstName : "",  
            localTime: "", 
            transactions : [],  
            activeClass: 'text-success',
            errorClass: 'text-danger'      
        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients/current")
        .then(response => {
            console.log(response)      
                      
            this.firstName =response.data.firstName; 
            console.log(this.firstName)

            let id = location.search;
            console.log(id) 
            let idAccount = new URLSearchParams(id);
            console.log(idAccount)   
            let sku = idAccount.get('id');   
            this.account = response.data.accounts.find( account =>  account.id == sku);
            console.log(this.account)
            
            this.transactions = this.account.transactions.sort((a, b) => b.id - a.id);
            console.log(this.transactions)
            
        })
        .catch(error => console.log(error));

        this.updateLocalTime();   
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {
          return `${this.firstName}`;
        },
    },

    methods: {  

        formatCurrency(amount) {            
            return amount.toLocaleString('en-US', { style: 'currency', currency: 'USD' });
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

        formatTime(date) {
            const formattedDate = new Date(date);
            const options = { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: false };
            return formattedDate.toLocaleTimeString('en-US', options);
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