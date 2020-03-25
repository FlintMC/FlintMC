package net.labyfy.sentry;


import io.sentry.Sentry;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;
import net.labyfy.component.tasks.Task;
import net.labyfy.component.tasks.Tasks;
import net.labyfy.component.tasks.subproperty.TaskBody;
import net.labyfy.component.tasks.subproperty.TaskBodyPriority;

import javax.inject.Singleton;

@Singleton
@Task(Tasks.PRE_MINECRAFT_INITIALIZE)
@TaskBodyPriority()
public class LabyfySentryClient {

    @TaskBody
    private void init(){
        System.out.println("DEBUUUUUUUUUUUUUUUUUUG CALLED");
        /*
         It is recommended that you use the DSN detection system, which
         will check the environment variable "SENTRY_DSN", the Java
         System Property "sentry.dsn", or the "sentry.properties" file
         in your classpath. This makes it easier to provide and adjust
         your DSN without needing to change your code. See the configuration
         page for more information.

         For example, using an environment variable

         export SENTRY_DSN="http://a46d935613f74b87b6faaf7611e7c231@31.172.80.159:9000/1"
         */
        //Sentry.init();

        // You can also manually provide the DSN to the ``init`` method.
        // #/1 internal #/2 labyfy
        Sentry.init("https://413b8f3fc06b407f9e8b1f5bd41258eb@sentry.labymod.net/2");

        /*
         It is possible to go around the static ``Sentry`` API, which means
         you are responsible for making the LabyfySentryClient instance available
         to your code.
         */

        logWithStaticAPI();
    }

    /**
     * An example method that throws an exception.
     */
    void unsafeMethod() {
        throw new UnsupportedOperationException("You shouldn't call this!");
    }

    /**
     * Examples using the (recommended) static API.
     */
    void logWithStaticAPI() {
        // Note that all fields set on the context are optional. Context data is copied onto
        // all future events in the current context (until the context is cleared).

        // Record a breadcrumb in the current context. By default the last 100 breadcrumbs are kept.
        Sentry.getContext().recordBreadcrumb(
                new BreadcrumbBuilder().setMessage("Xiantiel startet his client").build()
        );

        // Set the user in the current context.
        Sentry.getContext().setUser(
                new UserBuilder().setEmail("xiantiel").build()
        );

        // Add extra data to future events in this context.
        Sentry.getContext().addExtra("Coolness", "over 9000");

        // Add an additional tag to future events in this context.
        Sentry.getContext().addTag("shouldFail", "true");
        System.out.println("DEBUG: collected data");
        Sentry.capture("Hi im here!");
        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Sentry using the statically stored instance
            // that was created in the ``main`` method.
            Sentry.capture(e);
        }
    }
}