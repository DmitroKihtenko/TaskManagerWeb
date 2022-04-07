package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

public class LinkedTaskList extends AbstractTaskList {
    /**
     * Saving cell class for linked list node.
     */
    class LinkedListPointer {
        Task storedTask;
        LinkedListPointer next;
    }

    private int size;
    private LinkedListPointer first;

    public LinkedTaskList() {
        first = new LinkedListPointer();
    }

    @Override
    public void add(Task task) {
        if(task == null) {
            throw new IllegalArgumentException(
                    "Task object parameter has null value!"
            );
        }

        LinkedListPointer tempPointer = first;

        first.storedTask = task;

        first = new LinkedListPointer();
        first.next = tempPointer;

        size++;
    }

    @Override
    public boolean remove(Task task) {
        if(task == null) {
            throw new IllegalArgumentException(
                    "Task object parameter has null value!"
            );
        }

        LinkedListPointer searchPointer = first;

        if(size == 0) {
            return false;
        }

        while(searchPointer.next != null) {
            if(searchPointer.next.storedTask.equals(task)) {
                searchPointer.next = searchPointer.next.next;

                size--;

                return true;
            }

            searchPointer = searchPointer.next;
        }

        return false;
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Any task adds to begin of list so task indexes is reversed
     */
    @Override
    public Task getTask(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Invalid LinkedTaskList index parameter!"
            );
        }

        index++;
        LinkedListPointer searchPointer = first;

        for(int counter = size; counter > size - index; counter--) {
            searchPointer = searchPointer.next;
        }

        return searchPointer.storedTask;
    }
}
