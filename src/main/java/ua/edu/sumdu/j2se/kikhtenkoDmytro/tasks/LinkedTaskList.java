package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

public class LinkedTaskList {
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

    public int size() {
        return size;
    }

    /**
     * Any task adds to begin of list so task indexes is reversed
     */
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

    public LinkedTaskList incoming(int from, int to) {
        if(from > to) {
            throw new IllegalArgumentException(
                    "Invalid interval parameters!"
            );
        }

        int nextTaskTime;
        LinkedListPointer searchPointer = new LinkedListPointer();
        LinkedTaskList returnList = new LinkedTaskList();

        while(searchPointer.next != null) {
            searchPointer = searchPointer.next;

            nextTaskTime = searchPointer.storedTask.nextTimeAfter(from);

            if(nextTaskTime != -1 && nextTaskTime < to) {
                returnList.add(searchPointer.storedTask);
            }
        }

        return returnList;
    }
}
