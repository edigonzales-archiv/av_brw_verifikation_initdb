CREATE TABLE lfp3_ausserhalb
(
  ogc_fid serial NOT NULL,
  nummer character varying,
  nbident character varying,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT lfp_ausserhalb_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('lfp3_ausserhalb','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POINT',2);

CREATE INDEX idx_lfp3_ausserhalb_gem_bfs
  ON lfp3_ausserhalb
  USING btree
  (gem_bfs);

CREATE INDEX idx_lfp3_ausserhalb_nummer
  ON lfp3_ausserhalb
  USING btree
  (nummer);

CREATE INDEX idx_lfp3_ausserhalb_ogc_fid
  ON lfp3_ausserhalb
  USING btree
  (ogc_fid);

CREATE INDEX idx_lfp3_ausserhalb_the_geom
  ON lfp3_ausserhalb
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/

CREATE TABLE nbgeometrie_nicht_gerunded
(
  ogc_fid serial NOT NULL,
  kt_txt character varying,
  nbnummer character varying,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT nbgeometrie_nicht_gerunded_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('nbgeometrie_nicht_gerunded','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_nbgeometrie_nicht_gerunded_gem_bfs
  ON nbgeometrie_nicht_gerunded
  USING btree
  (gem_bfs);

CREATE INDEX idx_nbgeometrie_nicht_gerunded_ogc_fid
  ON nbgeometrie_nicht_gerunded
  USING btree
  (ogc_fid);

CREATE INDEX idx_nbgeometrie_nicht_gerunded_the_geom
  ON nbgeometrie_nicht_gerunded
  USING gist
  (the_geom);
  
/*-------------------------------------------------------------*/
  
CREATE TABLE bdbed_nicht_in_nf
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT bdbed_nicht_in_nf_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('bdbed_nicht_in_nf','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_bdbed_nicht_in_nf_gem_bfs
  ON nbgeometrie_nicht_gerunded
  USING btree
  (gem_bfs);

CREATE INDEX idx_bdbed_nicht_in_nf_ogc_fid
  ON nbgeometrie_nicht_gerunded
  USING btree
  (ogc_fid);

CREATE INDEX idx_bdbed_nicht_in_nf_the_geom
  ON nbgeometrie_nicht_gerunded
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/

CREATE TABLE bdbed_nicht_in_ig
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT bdbed_nicht_in_ig_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('bdbed_nicht_in_ig','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_bdbed_nicht_in_ig_gem_bfs
  ON bdbed_nicht_in_ig
  USING btree
  (gem_bfs);

CREATE INDEX idx_bdbed_nicht_in_ig_ogc_fid
  ON bdbed_nicht_in_ig
  USING btree
  (ogc_fid);

CREATE INDEX idx_bdbed_nicht_in_ig_the_geom
  ON bdbed_nicht_in_ig
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/

CREATE TABLE liegen_nicht_in_nf
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT liegen_nicht_in_nf_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('liegen_nicht_in_nf','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_liegen_nicht_in_nf_gem_bfs
  ON liegen_nicht_in_nf
  USING btree
  (gem_bfs);

CREATE INDEX idx_liegen_nicht_in_nf_ogc_fid
  ON liegen_nicht_in_nf
  USING btree
  (ogc_fid);

CREATE INDEX idx_liegen_nicht_in_nf_the_geom
  ON liegen_nicht_in_nf
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/

CREATE TABLE liegen_nicht_in_ig
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT liegen_nicht_in_ig_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('liegen_nicht_in_ig','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_liegen_nicht_in_ig_gem_bfs
  ON liegen_nicht_in_ig
  USING btree
  (gem_bfs);

CREATE INDEX idx_liegen_nicht_in_ig_ogc_fid
  ON liegen_nicht_in_ig
  USING btree
  (ogc_fid);

CREATE INDEX idx_liegen_nicht_in_ig_the_geom
  ON liegen_nicht_in_ig
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/
  
CREATE TABLE gemgre_nicht_in_nf
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT gemgre_nicht_in_nf_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('gemgre_nicht_in_nf','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_gemgre_nicht_in_nf_gem_bfs
  ON gemgre_nicht_in_nf
  USING btree
  (gem_bfs);

CREATE INDEX idx_gemgre_nicht_in_nf_ogc_fid
  ON gemgre_nicht_in_nf
  USING btree
  (ogc_fid);

CREATE INDEX idx_gemgre_nicht_in_nf_the_geom
  ON gemgre_nicht_in_nf
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/
  
CREATE TABLE gemgre_nicht_in_ig
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT gemgre_nicht_in_ig_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('gemgre_nicht_in_ig','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_gemgre_nicht_in_ig_gem_bfs
  ON gemgre_nicht_in_ig
  USING btree
  (gem_bfs);

CREATE INDEX idx_gemgre_nicht_in_ig_ogc_fid
  ON gemgre_nicht_in_ig
  USING btree
  (ogc_fid);

CREATE INDEX idx_gemgre_nicht_in_ig_the_geom
  ON gemgre_nicht_in_ig
  USING gist
  (the_geom);

/*-------------------------------------------------------------*/
  
CREATE TABLE gemgre_mit_liegen_flaeche
(
  ogc_fid serial NOT NULL,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT gemgre_nicht_in_ig_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

SELECT AddGeometryColumn('gemgre_mit_liegen_flaeche','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POLYGON',2);

CREATE INDEX idx_gemgre_mit_liegen_flaeche_gem_bfs
  ON gemgre_nicht_in_ig
  USING btree
  (gem_bfs);

CREATE INDEX idx_gemgre_mit_liegen_flaeche_ogc_fid
  ON gemgre_nicht_in_ig
  USING btree
  (ogc_fid);

CREATE INDEX idx_gemgre_mit_liegen_flaeche_the_geom
  ON gemgre_nicht_in_ig
  USING gist
  (the_geom);
  
/*-------------------------------------------------------------*/
  
CREATE OR REPLACE VIEW v_liegenschaften_projliegenschaft AS 
 SELECT l.ogc_fid,
    g.nbident,
    g.nummer,
    g.egris_egrid,
    g.gueltigkeit,
    g.vollstaendigkeit,
    g.art_txt,
    l.flaechenmass,
    l.geometrie,
    l.gem_bfs,
    l.lieferdatum
   FROM liegenschaften_projgrundstueck g,
    liegenschaften_projliegenschaft l
  WHERE g.ogc_fid = l.projliegenschaft_von;
  
/*-------------------------------------------------------------*/

CREATE OR REPLACE VIEW v_liegenschaften_projselbstrecht AS 
 SELECT l.ogc_fid,
    g.nbident,
    g.nummer,
    g.egris_egrid,
    g.gueltigkeit_txt,
    g.vollstaendigkeit_txt,
    g.art_txt,
    l.flaechenmass,
    l.geometrie,
    l.gem_bfs,
    l.lieferdatum    
   FROM liegenschaften_projgrundstueck g,
    liegenschaften_projselbstrecht l
  WHERE g.ogc_fid = l.projselbstrecht_von;
