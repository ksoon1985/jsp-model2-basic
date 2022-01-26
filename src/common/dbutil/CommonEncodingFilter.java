package common.dbutil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CommonEncodingFilter implements Filter {

    /**
     * 인코딩을 수행할 인코딩 캐릭터 셋 지정
     */
    private String encoding = null;

    /**
     * 필터 설정 관리자
     */
    protected FilterConfig filterConfig = null;

	
    /**
     * @return
     */
    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    /**
     * @param cfg
     */
    public void setFilterConfig(FilterConfig cfg) {
        filterConfig = cfg;
    }

	@Override
	public void destroy() {
		this.encoding = null;
	    this.filterConfig = null;

	}

	@Override
	public void doFilter(ServletRequest request, 
			   ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		  
		if (request.getCharacterEncoding() == null) {
	            if (encoding != null) {
	              request.setCharacterEncoding(encoding);
	            }
	        }

	        chain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    this.filterConfig = filterConfig;
	    this.encoding = filterConfig.getInitParameter("encoding");

	}

}
