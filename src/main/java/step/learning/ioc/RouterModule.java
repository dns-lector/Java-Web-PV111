package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.AuthFilter;
import step.learning.filters.DbFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through( DbFilter.class );
        filter("/*").through( AuthFilter.class );

        serve("/"      ).with( HomeServlet.class    ) ;
        serve("/auth"  ).with( AuthServlet.class    ) ;
        serve("/ioc"   ).with( IocServlet.class     ) ;
        serve("/signup").with( SignupServlet.class  ) ;
    }
}
