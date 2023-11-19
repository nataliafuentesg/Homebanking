let { createApp } = Vue
console.log("Cards Create")

const options = {
    data() {
        return {
            selectedType: 'DEBIT',
            selectedColor: 'SILVER',
            errorMessage: '',
            firstName : '',
            localTime : '' ,
                      
        }
    },

    created() {
        axios.get("/api/clients/current")
            .then(response => {
                console.log(response)               
                this.firstName = response.data.firstName;                
                console.log(this.firstName)
                
                
            })
            .catch(error => console.log(error));

        this.updateLocalTime();
        setInterval(this.updateLocalTime, 1000);
    },

    computed: {
        welcomeMessage() {
            return `${this.firstName}`;
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

        createCard() {
            
            const dataCard = `type=${(this.selectedType)}&color=${(this.selectedColor)}`;
            console.log(dataCard)

            axios.post('/api/clients/current/cards', dataCard, {
                headers: {'content-type': 'application/x-www-form-urlencoded'}
              })
                .then(response => {
                    if (response.status === 201) {
                        window.location.href = '/web/cards.html';
                    }
                })
                .catch(error => {                    
                    this.errorMessage = error.response.data;
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