import 'bootstrap/dist/css/bootstrap.css';
import './main.css';

import Vue from 'vue';
import ToDoApplication from './components/ToDoApplication';

Vue.config.productionTip = false;

Vue.prototype.$showError = function(message) {
    this.$root.errors.unshift(message);
};

new Vue({
    data: {errors: []},
    el: '#to-do-application',
    render: h => h(ToDoApplication),
});
