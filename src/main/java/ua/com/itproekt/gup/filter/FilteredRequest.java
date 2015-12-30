package ua.com.itproekt.gup.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class FilteredRequest extends HttpServletRequestWrapper {
    private Map<String, String[]> modifiableParameters;
    private Map<String, String[]> allParameters = null;

    public FilteredRequest(HttpServletRequest request) {
        super(request);
        modifiableParameters = new TreeMap<>();
    }

    /**
     * Create a new request wrapper that will merge additional parameters into
     * the request object without prematurely reading parameters from the
     * original request.
     *
     * @param request
     * @param additionalParams
     */
    public FilteredRequest(final HttpServletRequest request,
                                     final Map<String, String[]> additionalParams) {
        super(request);
        modifiableParameters = new TreeMap<>();
        modifiableParameters.putAll(additionalParams);
    }

    public void addParameters(Map<String, String[]> additionalParams) {
        modifiableParameters.putAll(additionalParams);
    }

    @Override
    public String getParameter(final String name) {
        String[] strings = getParameterMap().get(name);
        if (strings != null) {
            return strings[0];
        }
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (allParameters == null) {
            allParameters = new TreeMap<>();
            allParameters.putAll(super.getParameterMap());
            allParameters.putAll(modifiableParameters);
        }
        //Return an unmodifiable collection because we need to uphold the interface contract.
        return Collections.unmodifiableMap(allParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(final String name) {
        return getParameterMap().get(name);
    }
}
