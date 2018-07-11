package victorpc.projetopdm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonGeral {

    @SerializedName("photos")
    @Expose
    ObjetoJson json;

    public ObjetoJson getJson() {
        return json;
    }

    public void setJson(ObjetoJson json) {
        this.json = json;
    }
}
