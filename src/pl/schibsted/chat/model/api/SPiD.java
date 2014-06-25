package pl.schibsted.chat.model.api;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * @author krzysztof.kosobudzki
 */
public interface SPiD {
    @FormUrlEncoded
    @POST("/api/oauth/exchange")
    SPiDOneTimeResponse getOneTimeCode(@Field("clientId") String clientId, @Field("oauth_token") String oauthToken, @Field("type") String code);
}
