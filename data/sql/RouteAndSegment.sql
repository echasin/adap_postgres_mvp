Select distinct d.id, e.name, b.assetorigin_id, g1.name,     i.latitudedd, c.assetdestination_id, g2.name, first, last, h.id, h.name from (
Select s.route_id AS routeid,s.id AS segmentid, min(s.segmentnumber) AS first, max(s.segmentnumber)AS last from segment s GROUP BY s.route_id, s.id) a, 
segment b, segment c, route d, objclassification e, objcategory f, asset g1, asset g2, objtype h, location i, segment x
where (a.segmentid = b.id AND b.segmentnumber= first)
and (a.segmentid = c.id AND b.segmentnumber= last)
and a.routeid = d.id
and d.objclassification_id = e.id
and d.objcategory_id = f.id
and (b.assetorigin_id = g1.id and b.segmentnumber = first and b.route_id = c.route_id)
and (c.assetdestination_id = g2.id and c.segmentnumber = last and c.route_id = b.route_id )
and g1.objtype_id = h.id
order by d.id

UNION

/*SEGMENT VALIDATION QUERY */
Select distinct d.id, e.name, b.assetorigin_id, g1.name, i.cityname, c.assetdestination_id, g2.name, first, last, h.id, h.name from (
Select s.route_id AS routeid,s.id AS segmentid, min(s.segmentnumber) AS first, max(s.segmentnumber)AS last from segment s GROUP BY s.route_id, s.id) a, 
segment b, segment c, route d, objclassification e, objcategory f, asset g1, asset g2, objtype h, location i
where (a.segmentid = b.id AND b.segmentnumber= first)
and (a.segmentid = c.id AND b.segmentnumber= last)
and a.routeid = d.id
and d.objclassification_id = e.id
and d.objcategory_id = f.id
and b.assetorigin_id = g1.id 
and i.asset_id = g1.id
and c.assetdestination_id = g2.id 
and g1.objtype_id = h.id
order by d.id; 

/* GET SEGMENTS BY ROUTE.ID */
Select s.route_id, s.segmentnumber, ao.name as ORIGIN, lo.cityname,lo.latitudedd, lo.longitudedd,  ad.name as DEST, lo.cityname, ld.latitudedd, ld.longitudedd
from segment s, asset ao, asset ad, location lo, location ld
where 
s.route_id = 5201
and s.assetorigin_id = ao.id
and s.assetdestination_id = ad.id
and (s.assetorigin_id = lo.asset_id and lo.isprimary = true)
and (s.assetdestination_id = ld.asset_id and ld.isprimary = true)




SELECT * FROM
(
Select s1.route_id, s1.id as c1, 'A' AS C2 from segment s1
WHERE s1
Union
Select s2.route_id, S2.id as c1, 'b' AS c2 from segment s2
)
WHERE S1.ROUTE_ID = S2.route_id






















Select s.route_id AS routeid,s.id AS segmentid, min(s.segmentnumber) AS first, max(s.segmentnumber)AS last from segment s GROUP BY s.route_id, s.id) a, 
segment b, segment c
where (a.segmentid = b.id AND b.segmentnumber= first)
and (a.segmentid = c.id AND b.segmentnumber= last)


Select a.assetorgin_id from 
(Select s.route_id AS routeid, s.id AS segmentid, min(s.segmentnumber) AS ORIGIN from segment s GROUP BY s.route_id , s.id) a
where routeid = 5201;