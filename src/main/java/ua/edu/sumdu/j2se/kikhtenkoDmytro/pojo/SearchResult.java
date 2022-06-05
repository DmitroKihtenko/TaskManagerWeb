package ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.Amount;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.ObjectType;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.types.VerifiedCustomType;

import java.util.Objects;

public class SearchResult<T> implements Replicable {
    private final VerifiedCustomType<Integer> total;
    private final VerifiedCustomType<Integer> current;
    private final VerifiedCustomType<Integer> from;
    private final ObjectType<T> result;

    protected void checkIntegrity() {
        Integer totalVal = total.getValue();
        Integer currentVal = current.getValue();
        Integer fromVal = from.getValue();

        if(totalVal != null) {
            if (currentVal != null) {
                if(currentVal > totalVal) {
                    throw new IllegalArgumentException(
                            "Current search amount can " +
                                    "not be more than total");
                }
                if(fromVal != null) {
                    if(fromVal + currentVal > totalVal) {
                        throw new IllegalArgumentException(
                                "Amount of results with current" +
                                        " value out of the total");
                    }
                }
            }
        }
    }

    public SearchResult() {
        total = new Amount("Total amount", null,true) {
            @Override
            public void checkValue(Integer value) {
                super.checkValue(value);
                checkIntegrity();
            }
        };
        current = new Amount("Total amount", null,true) {
            @Override
            public void checkValue(Integer value) {
                super.checkValue(value);
                checkIntegrity();
            }
        };
        from = new Amount("Total amount", null,true) {
            @Override
            public void checkValue(Integer value) {
                super.checkValue(value);
                checkIntegrity();
            }
        };
        result = new ObjectType<>("Search result", null,true);
    }

    public Integer getTotal() {
        return total.getValue();
    }

    public void setTotal(Integer total) {
        this.total.setValue(total);
        this.total.setAdjusted(true);
    }

    public Integer getCurrent() {
        return current.getValue();
    }

    public void setCurrent(Integer current) {
        this.current.setValue(current);
        this.current.setAdjusted(true);
    }

    public Integer getFrom() {
        return from.getValue();
    }

    public void setFrom(Integer from) {
        this.from.setValue(from);
        this.from.setAdjusted(true);
    }

    public T getResult() {
        return result.getValue();
    }

    public void setResult(T result) {
        this.result.setValue(result);
        this.result.setAdjusted(true);
    }

    @JsonIgnore
    public VerifiedCustomType<Integer> getTotalCover() {
        return total;
    }

    @JsonIgnore
    public VerifiedCustomType<Integer> getCurrentCover() {
        return current;
    }

    @JsonIgnore
    public VerifiedCustomType<Integer> getFromCover() {
        return from;
    }

    @JsonIgnore
    public ObjectType<T> getResultCover() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SearchResult)) {
            return false;
        }
        SearchResult<?> that = (SearchResult<?>) o;
        return total.equals(that.total) &&
                current.equals(that.current) &&
                from.equals(that.from) &&
                result.equals(that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, current, from, result);
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "total=" + total +
                ", current=" + current +
                ", from=" + from +
                ", result=" + result +
                '}';
    }

    @Override
    public SearchResult<T> replicate() {
        SearchResult<T> object = new SearchResult<>();
        object.update(this);
        return object;
    }

    @Override
    public void update(Object object) {
        if(object instanceof SearchResult) {
            SearchResult<T> value = (SearchResult<T>) object;
            if(value.getTotalCover().isAdjusted()) {
                setTotal(value.getTotal());
            }
            if(value.getCurrentCover().isAdjusted()) {
                setCurrent(value.getCurrent());
            }
            if(value.getFromCover().isAdjusted()) {
                setFrom(value.getFrom());
            }
            if(value.getResultCover().isAdjusted()) {
                setResult(value.getResult());
            }
        }
    }
}
