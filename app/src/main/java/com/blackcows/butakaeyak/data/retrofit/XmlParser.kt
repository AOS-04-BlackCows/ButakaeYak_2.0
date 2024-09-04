package com.blackcows.butakaeyak.data.retrofit

import android.util.Log
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import javax.xml.parsers.DocumentBuilderFactory

const private val TAG = "PharmacyInfo_XMLPARSER"
class XmlParser {
    fun Paser(url: String) {
        var page = 1 // 페이지 초기값
        var count = 0
        try {
            while (true) {
                // parsing할 url 지정(API 키 포함해서)

                val dbFactoty = DocumentBuilderFactory.newInstance()
                val dBuilder = dbFactoty.newDocumentBuilder()
                val doc: Document = dBuilder.parse(url + page)


                // root tag
                doc.getDocumentElement().normalize()
                Log.d(TAG,"Root element :" + doc.getDocumentElement().getNodeName())


                // 파싱할 tag
                val nList: NodeList = doc.getElementsByTagName("item")
                Log.d(TAG,"파싱할 리스트 수 : " + nList.length)

                for (temp in 0 until nList.length) {
                    val nNode = nList.item(temp)
                    if (nNode.nodeType == Node.ELEMENT_NODE) {
                        val eElement: Element = nNode as Element
                        Log.d(TAG,"######################")
                        Log.d(TAG,eElement.getTextContent());
                        Log.d(TAG,"기관명 : " + getTagValue("dutyName", eElement))
                        Log.d(TAG,"주소  : " + getTagValue("dutyAddr", eElement))
                        Log.d(TAG,"전화번호 : " + getTagValue("dutyTel1", eElement))
                        Log.d(TAG,"진료시간  : " + getTagValue("dutyTime1s", eElement) + " - " + getTagValue("dutyTime1c", eElement))
                        Log.d(TAG,"위도 : " + getTagValue("wgs84Lon", eElement));
                        Log.d(TAG,"경도 : " + getTagValue("wgs84Lat", eElement));
                        count++
                    } // for end
                } // if end


                page += 1
                Log.d(TAG,"page number : $page")

                if (page > 12) {
                    break
                }
            } // while end

            Log.d(TAG,"count = $count")
        } catch (e: Exception) {
            e.printStackTrace()
        } // try~catch end
    }

    companion object {
        private fun getTagValue(tag: String, eElement: Element): String? {
            val nlList: NodeList = eElement.getElementsByTagName(tag).item(0).getChildNodes()
            val nValue: Node = nlList.item(0) as Node ?: return null
            return nValue.nodeValue
        }

        @JvmStatic
        fun main(args: Array<String>) {
            // TODO Auto-generated method stub
            val xml = XmlParser()
            val url = "URL^^"
            xml.Paser(url)
        }
    }
}