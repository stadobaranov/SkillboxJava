<template>
    <div class="to-do-application">
        <to-do-list>
            <to-do-task
                slot="task"
                slot-scope="slot"
                :key="slot.task.dirty.id"
                :record="slot.task"
                :remover="slot.remover"></to-do-task>
        </to-do-list>

        <div class="to-do-application__errors">
            <div v-for="(error, i) in errors" :key="error" class="alert alert-danger">
                <h4 class="alert-heading">Упс, что-то пошло не так</h4>
                <p>{{ error }}</p>
                <hr>
                <button
                    class="btn btn-block btn-outline-danger"
                    type="button"
                    @click="errors.splice(i, 1)"
                >Закрыть</button>
            </div>
        </div>
    </div>
</template>

<style>
    .to-do-application {
        min-height: 100%;
        padding: 1em;
        background-color: #fc6e51;
    }

    .to-do-application__errors {
        position: fixed;
        top: 1em;
        right: 1em;
        width: 360px;
    }
</style>

<script>
    import ToDoList from './ToDoList';
    import ToDoTask from './ToDoTask';

    export default {
        components: {
            'to-do-list': ToDoList,
            'to-do-task': ToDoTask,
        },

        computed: {
            errors() {
                return this.$root.errors;
            },
        },
    };
</script>
