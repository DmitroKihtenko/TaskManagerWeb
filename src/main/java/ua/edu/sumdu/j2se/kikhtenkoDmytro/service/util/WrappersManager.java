package ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;

import java.util.LinkedList;
import java.util.List;

@Component
public class WrappersManager implements ServiceExceptionWrapper {
    private static final Logger logger =
            LoggerFactory.getLogger(WrappersManager.class);
    private List<ServiceExceptionWrapper> wrappers;

    public WrappersManager() {
        this.wrappers = new LinkedList<>();
    }

    public List<ServiceExceptionWrapper> getWrappers() {
        return wrappers;
    }

    @Autowired
    public void setWrappers(List<ServiceExceptionWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public ServiceException handle(Exception exception) {
        ServiceException result = null;
        for(ServiceExceptionWrapper wrapper : this.wrappers) {
            result = wrapper.handle(exception);
            if(result != null) {
                break;
            }
        }
        if(result == null) {
            logger.debug("No handlers found for error: " +
                    exception.getMessage());
        }
        return result;
    }
}
