package coms.w4156.moviewishlist.exceptions;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.GraphQLException;
import graphql.GraphqlErrorBuilder;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter;
import org.springframework.stereotype.Component;

@Component
public class GraphQLExceptionHandler
    extends DataFetcherExceptionResolverAdapter {

    /**
     * Handle custom GraphQL Errors and send them in the response.
     * @param ex - The throawable object.
     * @param env - The GraphQL Data Fetching Environment object.
     * @return - Exception to return in the GraphQL response.
     */
    @Override
    protected GraphQLError resolveToSingleError(
        final Throwable ex,
        final DataFetchingEnvironment env
    ) {
        if (ex instanceof GraphQLException) {
            return GraphqlErrorBuilder
                .newError()
                .errorType(ErrorType.DataFetchingException)
                .message(ex.getMessage())
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        } else {
            return GraphqlErrorBuilder
                .newError()
                .errorType(ErrorType.DataFetchingException)
                .message("Internal Server Error")
                .path(env.getExecutionStepInfo().getPath())
                .location(env.getField().getSourceLocation())
                .build();
        }
    }
}
