package ua.edu.sumdu.j2se.kikhtenkoDmytro.dao;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public abstract class TableQueries {
    private QueryProvider queryProvider;
    private QueryLoader queryLoader;

    public TableQueries(@NonNull QueryProvider provider,
                        @NonNull QueryLoader queryLoader) {
        queryProvider = provider;
        this.queryLoader = queryLoader;
    }


    public void setProvider(@NonNull QueryProvider provider) {
        queryProvider = provider;
    }

    @NonNull
    public QueryProvider getProvider() {
        return queryProvider;
    }

    @NonNull
    public QueryLoader getQueryLoader() {
        return queryLoader;
    }

    public void setQueryLoader(@NonNull QueryLoader queryLoader) {
        this.queryLoader = queryLoader;
    }
}
