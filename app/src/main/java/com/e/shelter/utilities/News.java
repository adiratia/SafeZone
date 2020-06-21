package com.e.shelter.utilities;

public class News {
    private String title;
    private String description;
    private String date;
    private String urlToImage;
    private String url;
    private String id;

    /**
     * Constructor
     * @param title
     * @param description
     * @param date
     * @param urlToImage
     * @param url
     * @param id
     */
    public News(String title, String description, String date, String urlToImage, String url, String id) {
        setTitle(title);
        setDescription(description);
        setDate(date);
        setUrlToImage(urlToImage);
        setUrl(url);
        setId(id);
    }

    /**
     * getting title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * setting title
     * @param title
     */
    private void setTitle(String title) {
        if (!title.equals("null")) this.title = title;
        else this.title = " ";

    }

    /**
     * getting description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * setting description
     * @param description
     */
    private void setDescription(String description) {
        if (!description.equals("null")) this.description = description;
        else this.description = " ";
    }

    /**
     * getting date
     * @return
     */
    public String getDate() {
        return date;
    }

    /**
     * setting date
     * @param date
     */
    private void setDate(String date) {
        if (!date.equals("null")) {
            this.date = date.replace("T", " ").replace("Z", "");
        }
        else this.date = "";
    }

    /**
     * getting url of image
     * @return
     */
    public String getUrlToImage() {
        return urlToImage;
    }

    /**
     * setting url ofimage
     * @param urlToImage
     */
    private void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    /**
     * getting url
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * setting url
     * @param url
     */
    private void setUrl(String url) {
        if (!url.equals("null")) this.url = url;
        else this.url = "";
    }

    /**
     * getting id of news
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * setting id of news
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}
