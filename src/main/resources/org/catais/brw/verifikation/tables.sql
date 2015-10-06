CREATE TABLE lfp3_ausserhalb
(
  ogc_fid serial NOT NULL,
  nummer character varying,
  bemerkung character varying,
  gem_bfs integer,
  lieferdatum date,
  CONSTRAINT lfp_ausserhalb_pkey PRIMARY KEY (ogc_fid)
)
WITH (
  OIDS=FALSE
);

-- SRS is hardcoded :(
SELECT AddGeometryColumn('lfp3_ausserhalb','the_geom',(SELECT srid FROM SPATIAL_REF_SYS WHERE AUTH_NAME='EPSG' AND AUTH_SRID=2056),'POINT',2);

CREATE INDEX idx_lfp3_ausserhalb_gem_bfs
  ON lfp_ausserhalb
  USING btree
  (gem_bfs);

CREATE INDEX idx_lfp3_ausserhalb_nummer
  ON lfp_ausserhalb
  USING btree
  (nummer);

CREATE INDEX idx_lfp3_ausserhalb_ogc_fid
  ON lfp_ausserhalb
  USING btree
  (ogc_fid);

CREATE INDEX idx_lfp3_ausserhalb_the_geom
  ON lfp_ausserhalb
  USING gist
  (the_geom);
