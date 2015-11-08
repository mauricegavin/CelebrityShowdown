package mauricegavin.celebrityshowdown.api;

import android.content.Context;
import android.os.Build;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;

import java.util.Locale;

import mauricegavin.celebrityshowdown.api.retrofit.ApiService;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by maurice on 30/09/15.
 */
public class ApiServiceBuilder {

    private Context context;
    private String authToken;

    /**
     * @param context the context is required for inflating resources
     */
    public ApiServiceBuilder(Context context) {
        this.context = context;
    }

    /**
     * @param authToken (optional) if there is an auth token for the user it is passed here
     * @return the builder object
     */
    public ApiServiceBuilder setAuthToken(String authToken) {
        this.authToken = authToken;
        return this;
    }

    /**
     * Create an ApiService object which the user can use to GET, POST and PATCH the endpoints on
     * OralEye backend servers.
     * @return an ApiService object
     */
    public ApiService create() {

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ApiService.API_ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL);

        // Configure SSL settings
        OkHttpClient client = new OkHttpClient();
        builder.setClient(new OkClient(client));

        // Create custom JSON parser (needed to handle ISO 8601 format timestamps)
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();
        builder.setConverter(new GsonConverter(gson));

        //builder.setErrorHandler(new RetrofitErrorHandler(context));

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("User-Agent", "(Linux; Android " + android.os.Build.VERSION.SDK_INT + "; " +
                        "Locale " + Locale.getDefault() + "; " +
                        "Manufacturer " + Build.MANUFACTURER + "; " +
                        "Device " + Build.DEVICE + "; " +
                        "Model " + Build.MODEL + "; " +
                        "Brand " + Build.BRAND + ")" //+
                        //"OralEye-Android/" + BuildConfig.VERSION_NAME + " " +
                        //"Flavour " + BuildConfig.FLAVOR
                        );

            }
        });

        return builder.build().create(ApiService.class);
    }

}
