let { createApp } = Vue
console.log("Cards")

const options = {
    data() {
        return {
            cards: [],
            firstName: "",
            localTime: "",   
            canCreateCard: false,         
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)
                this.cards = response.data.cards;
                this.firstName = response.data.firstName;
                console.log(this.firstName)
                this.canCreateCard = response.data.cards.length < 6;
            })
            .catch(error => console.log(error));

        this.updateLocalTime();
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {

            return `${this.firstName}`;
        },
        creditCards() {
            return this.cards.filter(card => card.cardType === 'CREDIT');
        },
        debitCards() {
            return this.cards.filter(card => card.cardType === 'DEBIT');
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

        formattedDate(date) {
            const formattedDate = new Date(date);
            const day = formattedDate.getDate().toString().padStart(2, "0");
            const month = (formattedDate.getMonth() + 1).toString().padStart(2, "0");
            const year = formattedDate.getFullYear().toString().slice(-2);
            return `${month}/${year}`;
        },

        uppercaseName(nameclient) {
            return nameclient.toUpperCase();
        },

        confirmDeleteCard(cardId) {
            if (window.confirm("Are you sure you want to delete this card?")) {
                this.deactivateCard(cardId);
            }
        },
        

        deactivateCard(cardId) {
            const data = `cardId=${(cardId)}`
            axios.patch(`/api/clients/current/cards/deactivate`, data, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
            })
              .then(response => {
                if (response.status === 200) {
                    window.location.reload();
                }
                console.log(response);
              })
              .catch(error => {
                console.log(error);
              });
        },

        logout() {
            axios.post('/api/logout')
                .then(response => {
                    if (response.status === 200) {
                        window.location.href = '/web/assets/pages/register.html';
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

