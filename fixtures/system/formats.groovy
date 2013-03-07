import cinnamon.Format

fixture{
    
    pdf(Format, name:'format.pdf', extension:'pdf', contenttype:'application/pdf', defaultObjectType:null)
    txt(Format, name:'format.txt', extension:'txt', contenttype:'text/plain', defaultObjectType:null)
    fm(Format, name:'format.fm', extension:'fm', contenttype:'application/fm', defaultObjectType:null)
    clp(Format, name:'format.clp', extension:'clp', contenttype:'application/clp', defaultObjectType:null)
    zip(Format, name:'format.zip', extension:'zip', contenttype:'application/zip', defaultObjectType:null)
    csv(Format, name:'format.csv', extension:'csv', contenttype:'text/csv', defaultObjectType:null)
    cdr(Format, name:'format.cdr', extension:'cdr', contenttype:'application/cdr', defaultObjectType:null)
    
    // Images:
    jpeg(Format, name:'format.jpeg', extension:'jpeg', contenttype:'image/jpeg', defaultObjectType:null)    
    png(Format, name:'format.png', extension:'png', contenttype:'image/png', defaultObjectType:null)
    gif(Format, name:'format.gif', extension:'gif', contenttype:'image/gif', defaultObjectType:null)
    eps(Format, name:'format.eps', extension:'eps', contenttype:'image/eps', defaultObjectType:null)
    tiff(Format, name:'format.tiff', extension:'tiff', contenttype:'image/tiff', defaultObjectType:null)
    cgm(Format, name:'format.cgm', extension:'cgm', contenttype:'image/cgm', defaultObjectType: null)
    
    // HTML:
    html(Format, name:'format.html', extension:'html', contenttype:'text/html', defaultObjectType:null)
    dtd(Format, name:'format.dtd', extension:'dtd', contenttype:'application/xml-dtd', defaultObjectType:null)
    css(Format, name:'format.css', extension:'css', contenttype:'text/css', defaultObjectType:null)
    js(Format, name:'format.js', extension:'js', contenttype: 'text/js', defaultObjectType: null)
    
    // XML:
    xml(Format, name:'format.xml', extension:'xml', contenttype:'application/xml', defaultObjectType:null)
    xsd(Format, name:'format.xsd', extension:'xsd', contenttype:'application/xml', defaultObjectType:null)
    xhtml(Format, name:'format.xhtml', extension:'xhtml', contenttype:'application/xhtml+xml', defaultObjectType:null)
    
    // OpenOffice / LibreOffice:
    odt(Format, name:'format.odt', extension:'odt', contenttype:'application/x-vnd.oasis.opendocument.text', defaultObjectType:null)
    ods(Format, name:'format.ods', extension:'ods', contenttype:'application/vnd.oasis.opendocument.spreadsheet', defaultObjectType:null)
    odp(Format, name:'format.odp', extension:'odp', contenttype:'application/vnd.oasis.opendocument.presentation', defaultObjectType:null)
    odg(Format, name:'format.odg', extension:'odg', contenttype:'application/vnd.oasis.opendocument.graphics', defaultObjectType:null)
    odc(Format, name:'format.odc', extension:'odc', contenttype:'application/vnd.oasis.opendocument.chart', defaultObjectType:null)
    odf(Format, name:'format.odf', extension:'odf', contenttype:'application/vnd.oasis.opendocument.formula', defaultObjectType:null)
    odi(Format, name:'format.odi', extension:'odi', contenttype:'application/vnd.oasis.opendocument.image', defaultObjectType:null)
    odm(Format, name:'format.odm', extension:'odm', contenttype:'application/vnd.oasis.opendocument.text-master', defaultObjectType:null)
    odb(Format, name:'format.odb', extension:'odb', contenttype:'application/vnd.oasis.opendocument.database', defaultObjectType:null)
    ott(Format, name:'format.ott', extension:'ott', contenttype:'application/vnd.oasis.opendocument.text-template', defaultObjectType:null)
    ots(Format, name:'format.ots', extension:'ots', contenttype:'application/vnd.oasis.opendocument.spreadsheet-template', defaultObjectType:null)
    otp(Format, name:'format.otp', extension:'otp', contenttype:'application/vnd.oasis.opendocument.presentation-template', defaultObjectType:null)
    otg(Format, name:'format.otg', extension:'otg', contenttype:'application/vnd.oasis.opendocument.graphics-template', defaultObjectType:null)
    otc(Format, name:'format.otc', extension:'otc', contenttype:'application/vnd.oasis.opendocument.chart-template', defaultObjectType:null)
    otf(Format, name:'format.otf', extension:'otf', contenttype:'application/vnd.oasis.opendocument.formula-template', defaultObjectType:null)
    oti(Format, name:'format.oti', extension:'oti', contenttype:'application/vnd.oasis.opendocument.image-template', defaultObjectType:null)
    oth(Format, name:'format.oth', extension:'oth', contenttype:'application/vnd.oasis.opendocument.text-web', defaultObjectType:null)
    
    // Microsoft:
    doc(Format, name:'format.doc', extension:'doc', contenttype:'application/msword', defaultObjectType:null)
    xls(Format, name:'format.xls', extension:'xls', contenttype:'application/vnd.ms-excel', defaultObjectType:null)
    pps(Format, name:'format.pps', extension:'pps', contenttype:'application/vnd.ms-powerpoint', defaultObjectType:null)
    ppt(Format, name:'format.ppt', extension:'ppt', contenttype:'application/vnd.ms-powerpoint', defaultObjectType:null)
    xlsx(Format, name:'format.xlsx', extension:'xlsx', contenttype:'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', defaultObjectType:null)
    pptx(Format, name:'format.pptx', extension:'pptx', contenttype:'application/vnd.openxmlformats-officedocument.presentationml.presentation', defaultObjectType:null)
    docx(Format, name:'format.docx', extension:'docx', contenttype:'application/vnd.openxmlformats-officedocument.wordprocessingml.document', defaultObjectType:null)
    
    // S1000D related:
    ddn(Format, name:'format.ddn', extension:'ddn', contenttype:'application/xml', defaultObjectType: null)
    dm(Format, name:'format.dm', extension:'dm', contenttype:'application/xml', defaultObjectType: null)    
    
    // DITA related:
    dita(Format, name:'format.dita', extension:'dita', contenttype:'application/xml', defaultObjectType:null)
    ditamap(Format, name:'format.ditamap', extension:'ditamap', contenttype:'application/xml', defaultObjectType:null)
    
}