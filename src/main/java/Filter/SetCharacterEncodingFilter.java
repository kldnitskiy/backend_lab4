package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SetCharacterEncodingFilter implements Filter {
    protected String encoding = null;
    protected FilterConfig filterConfig = null;
    protected boolean ignore = true;
    public void destroy() {
        this.encoding = null;
        this.filterConfig = null;

    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {
        if (ignore || (request.getCharacterEncoding() == null)) {
            String characterEncoding = selectEncoding(request);
            if (characterEncoding != null)
                request.setCharacterEncoding(characterEncoding);
        }
        chain.doFilter(request, response);

    }
    public void init(FilterConfig fConfig) throws ServletException {

        this.filterConfig = fConfig;
        this.encoding = fConfig.getInitParameter("encoding");
        String value = fConfig.getInitParameter("ignore");
        if (value == null)
            this.ignore = true;
        else if (value.equalsIgnoreCase("true"))
            this.ignore = true;
        else if (value.equalsIgnoreCase("yes"))
            this.ignore = true;
        else
            this.ignore = false;

    }

    protected String selectEncoding(@SuppressWarnings("unused") ServletRequest request) {
        return (this.encoding);
    }


}
