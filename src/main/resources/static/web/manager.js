let { createApp } = Vue

const options = {
    data() {
        return {

            clients: [],
            firstName: '',
            lastName: '',
            email: '',
            jsonData: null,
        }
    },

    created() {
        this.loadData();
    },

    methods: {

        loadData() {
            axios.get("http://localhost:8080/rest/clients")
                .then(response => {
                    console.log(response)
                    console.log(response.data)
                    this.clients = response.data._embedded.clients;
                    console.log(this.clients)
                    this.jsonData = JSON.stringify(response.data, null, 1);
                })
                .catch(error => console.log(error));
        },

        addClient() {

            if (this.firstName && this.lastName && this.email) {
                if (this.clients.find(client => client.email === this.email)) {
                    window.alert('Client with the same email already exists.');
                    return;
                }
                this.postClient();
            } else {
                window.alert('Please fill in all required fields.');
            }
        },

        postClient() {
            const newClient = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email,
            };

            axios.post('http://localhost:8080/rest/clients', newClient)
                .then(response => {
                    console.log(response)
                    console.log(response.data);
                    this.clients.push(response.data);
                    
                    this.firstName = '';
                    this.lastName = '';
                    this.email = '';
                    this.loadData();
                })
                .catch(error => console.error('Error:', error));
        },

    }
}


const app = createApp(options)

app.mount('#app')