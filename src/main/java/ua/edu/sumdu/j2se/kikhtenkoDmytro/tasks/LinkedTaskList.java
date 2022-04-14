package ua.edu.sumdu.j2se.kikhtenkoDmytro.tasks;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedTaskList extends AbstractTaskList {
    /**
     * Saving cell class for linked list node.
     */
    class LinkedListPointer {
        Task storedTask;
        LinkedListPointer next;
    }

    private LinkedListPointer first;

    static {
        type = ListTypes.types.LINKED;
    }

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

        taskAmount++;
    }

    @Override
    public boolean remove(Task task) {
        if(task == null) {
            throw new IllegalArgumentException(
                    "Task object parameter has null value!"
            );
        }

        LinkedListPointer searchPointer = first;

        if(taskAmount == 0) {
            return false;
        }

        while(searchPointer.next != null) {
            if(searchPointer.next.storedTask.equals(task)) {
                searchPointer.next = searchPointer.next.next;

                taskAmount--;

                return true;
            }

            searchPointer = searchPointer.next;
        }

        return false;
    }

    /**
     * Any task adds to begin of list so task indexes is reversed
     */
    @Override
    public Task getTask(int index) {
        if(index < 0 || index >= taskAmount) {
            throw new IndexOutOfBoundsException(
                    "Invalid LinkedTaskList index parameter!"
            );
        }

        index++;
        LinkedListPointer searchPointer = first;

        for(int counter = taskAmount; counter > taskAmount - index; counter--) {
            searchPointer = searchPointer.next;
        }

        return searchPointer.storedTask;
    }

    public Iterator<Task> iterator() {
        return new Iterator<>() {
            private LinkedListPointer node = first.next;
            private LinkedListPointer deleteNode = first;

            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Task next() {
                if(node == null) {
                    throw new NoSuchElementException(
                            "Iterator reached last position!"
                    );
                }
                if(deleteNode.next.next == node) {
                    deleteNode = deleteNode.next;
                }
                node = node.next;
                return deleteNode.next.storedTask;
            }

            @Override
            public void remove() {
                if(node == first.next) {
                    throw new IllegalStateException(
                            "Needs calling of next() iterator method!"
                    );
                }
                deleteNode.next = node;
                taskAmount--;
            }
        };
    }

    @Override
    public LinkedTaskList clone() {
        LinkedTaskList returnObj = new LinkedTaskList();
        LinkedListPointer addPtr = first.next;

        while(addPtr != null) {
            returnObj.add(addPtr.storedTask);
            addPtr = addPtr.next;
        }
        return returnObj;
    }
}
