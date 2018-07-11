package victorpc.projetopdm;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Photo {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("owner")
    @Expose
    String owner;
    @SerializedName("secret")
    @Expose
    String secret;
    @SerializedName("server")
    @Expose
    String server;
    @SerializedName("farm")
    @Expose
    Integer farm;
    @SerializedName("title")
    @Expose
    String titulo;
    @SerializedName("ispublic")
    @Expose
    Integer ispublic;
    @SerializedName("isfriend")
    @Expose
    Integer isfriend;
    @SerializedName("isfamily")
    @Expose
    Integer isfamily;
    @SerializedName("url_m")
    @Expose
    String url;
    @SerializedName("height_m")
    @Expose
    String largura;
    @SerializedName("width_m")
    @Expose
    String comprimento;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getFarm() {
        return farm;
    }

    public void setFarm(Integer farm) {
        this.farm = farm;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getIspublic() {
        return ispublic;
    }

    public void setIspublic(Integer ispublic) {
        this.ispublic = ispublic;
    }

    public Integer getIsfriend() {
        return isfriend;
    }

    public void setIsfriend(Integer isfriend) {
        this.isfriend = isfriend;
    }

    public Integer getIsfamily() {
        return isfamily;
    }

    public void setIsfamily(Integer isfamily) {
        this.isfamily = isfamily;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLargura() {
        return largura;
    }

    public void setLargura(String largura) {
        this.largura = largura;
    }

    public String getComprimento() {
        return comprimento;
    }

    public void setComprimento(String comprimento) {
        this.comprimento = comprimento;
    }
}
