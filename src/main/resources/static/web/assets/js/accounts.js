

let { createApp } = Vue
console.log("Hola")

const options = {
    data() {
        return {
            accounts : [], 
            firstName : "",  
            localTime: "",         
        }
    },

    created() {
        axios.get("http://localhost:8080/api/clients/1")
        .then(response => {
            console.log(response)
            this.accounts = response.data.accounts;
            console.log(this.accounts)           
            this.firstName =response.data.firstName; 
            console.log(this.firstName)
        })
        .catch(error => console.log(error));

        this.updateLocalTime();   
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {

          return `¡Welcome back, ${this.firstName}!`;
        },
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

    }

}


const app = createApp(options)

app.mount('#app')

$(document).ready(function () {
    $('.dropdown').hover(function () {
        $(this).addClass('show');
        $(this).find('.dropdown-menu').addClass('show');
    }, function () {
        $(this).removeClass('show');
        $(this).find('.dropdown-menu').removeClass('show');
    });
});