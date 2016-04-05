package com.jannick.oslinux.tokens;

import java.util.List;

/**
 * Created by Jannick on 5-4-2016.
 */
public class KaKuToken {

    private List<KaKu> kakus;

    public List<KaKu> getKakus() {
        return kakus;
    }

    public void setKakus(List<KaKu> kakus) {
        this.kakus = kakus;
    }

    public static class KaKu {
        private int id;
        private String idletter;
        private int idnumber;
        private String status;
        private String location;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIdletter() {
            return idletter;
        }

        public void setIdletter(String idletter) {
            this.idletter = idletter;
        }

        public int getIdnumber() {
            return idnumber;
        }

        public void setIdnumber(int idnumber) {
            this.idnumber = idnumber;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }
}
