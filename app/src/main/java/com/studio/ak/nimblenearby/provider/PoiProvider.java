package com.studio.ak.nimblenearby.provider;

import android.net.Uri;

import de.triplet.simpleprovider.AbstractProvider;
import de.triplet.simpleprovider.Column;
import de.triplet.simpleprovider.Table;

/**
 * Created by Amal Krishnan on 09-12-2016.
 */

public class PoiProvider extends AbstractProvider {

    private static final String AUTHORITY="com.studio.ak.nimblenearby";
    static final String URL="content://"+AUTHORITY;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    @Override
    protected String getAuthority() {
        return AUTHORITY;
    }

    @Table
    public class Poi {

        @Column(value = Column.FieldType.TEXT, primaryKey = true)
        public static final String POI_ID = "_id";

        @Column(Column.FieldType.TEXT)
        public static final String POI_NAME = "name";

        @Column(Column.FieldType.TEXT)
        public static final String POI_VICINITY = "vicinity";

        @Column(Column.FieldType.TEXT)
        public static final String POI_ADDRESS = "address";

        @Column(Column.FieldType.TEXT)
        public static final String POI_PHONE = "phone";

        @Column(Column.FieldType.TEXT)
        public static final String POI_REVIEW = "review";

        @Column(Column.FieldType.TEXT)
        public static final String POI_LAT = "lat";

        @Column(Column.FieldType.TEXT)
        public static final String POI_LNG = "lng";

        @Column(Column.FieldType.INTEGER)
        public static final String POI_FAV="fav";
    }
}
