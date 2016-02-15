Select r.id,a.name,l.zippostcode,sf.matchvalue, sf.scorevalue from scorefactor sf, route r, segment s, location l, asset a
where (r.id =s.route_id 
and s.assetorigin_id = a.id
and a.id = l.asset_id
and s.segmentnumber = 1)
and (sf.matchattribute = 'location.zippostcode'
and sf.matchvalue = l.zippostcode)


Assetorgin_id 5201 is BOS
Select route_id from segment s where  s.segmentnumber = 1 and s.assetorigin_id = 5201