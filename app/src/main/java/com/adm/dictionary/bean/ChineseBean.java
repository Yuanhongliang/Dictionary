package com.adm.dictionary.bean;

import java.util.List;

/**
 * 返回为中文信息的Bean
 * Created by Administrator on 2016/10/18.
 */
public class ChineseBean {
    private String word_name;
    private List<Symbol> symbols;

    public String getWord_name() {
        return word_name;
    }

    public void setWord_name(String word_name) {
        this.word_name = word_name;
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }

    public class Symbol{
        private String word_symbol;
        private String symbol_mp3;
        private List<Parts> parts;
        private String ph_am_mp3;
        private String ph_en_mp3;
        private String ph_tts_mp3;
        private String ph_other;

        public String getWord_symbol() {
            return word_symbol;
        }

        public void setWord_symbol(String word_symbol) {
            this.word_symbol = word_symbol;
        }

        public String getSymbol_mp3() {
            return symbol_mp3;
        }

        public void setSymbol_mp3(String symbol_mp3) {
            this.symbol_mp3 = symbol_mp3;
        }

        public List<Parts> getParts() {
            return parts;
        }

        public void setParts(List<Parts> parts) {
            this.parts = parts;
        }

        public String getPh_am_mp3() {
            return ph_am_mp3;
        }

        public void setPh_am_mp3(String ph_am_mp3) {
            this.ph_am_mp3 = ph_am_mp3;
        }

        public String getPh_en_mp3() {
            return ph_en_mp3;
        }

        public void setPh_en_mp3(String ph_en_mp3) {
            this.ph_en_mp3 = ph_en_mp3;
        }

        public String getPh_tts_mp3() {
            return ph_tts_mp3;
        }

        public void setPh_tts_mp3(String ph_tts_mp3) {
            this.ph_tts_mp3 = ph_tts_mp3;
        }

        public String getPh_other() {
            return ph_other;
        }

        public void setPh_other(String ph_other) {
            this.ph_other = ph_other;
        }

        public class Parts{
            private String part_name;
            private List<Means> means;

            public String getPart_name() {
                return part_name;
            }

            public void setPart_name(String part_name) {
                this.part_name = part_name;
            }

            public List<Means> getMeans() {
                return means;
            }

            public void setMeans(List<Means> means) {
                this.means = means;
            }

            public class Means{
                private String word_mean;
                private String has_mean;
                private String split;

                public String getWord_mean() {
                    return word_mean;
                }

                public void setWord_mean(String word_mean) {
                    this.word_mean = word_mean;
                }

                public String getHas_mean() {
                    return has_mean;
                }

                public void setHas_mean(String has_mean) {
                    this.has_mean = has_mean;
                }

                public String getSplit() {
                    return split;
                }

                public void setSplit(String split) {
                    this.split = split;
                }
            }
        }
    }

}
