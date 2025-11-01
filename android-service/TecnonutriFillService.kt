package com.taskers.meal.assistant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.json.JSONArray
import org.json.JSONObject

/**
 * Serviço de Acessibilidade para automação do Tecnonutri
 * 
 * Este serviço preenche automaticamente refeições no app Tecnonutri
 * baseado em dados JSON recebidos via Intent ou notificação.
 */
class TecnonutriFillService : AccessibilityService() {

    companion object {
        private const val TAG = "TecnonutriFillService"
        private const val TECNONUTRI_PACKAGE = "br.com.tecnonutri"
        
        // IDs de recursos do Tecnonutri (podem variar entre versões)
        private const val ID_FAB_ADD = "com.android.systemui:id/fab"
        private const val ID_SEARCH_FIELD = "android.widget.EditText"
        private const val ID_QUANTITY_FIELD = "br.com.tecnonutri:id/quantity"
        private const val ID_SAVE_BUTTON = "br.com.tecnonutri:id/save_button"
    }

    private val handler = Handler(Looper.getMainLooper())
    private var currentMealData: JSONObject? = null
    private var currentItemIndex = 0

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                        AccessibilityEvent.TYPE_VIEW_CLICKED or
                        AccessibilityEvent.TYPE_VIEW_FOCUSED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS
            packageNames = arrayOf(TECNONUTRI_PACKAGE)
        }
        
        serviceInfo = info
        Log.i(TAG, "Serviço de preenchimento Tecnonutri iniciado")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            when (it.eventType) {
                AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                    if (it.packageName == TECNONUTRI_PACKAGE) {
                        Log.d(TAG, "Tecnonutri aberto: ${it.className}")
                        processMealDataIfAvailable()
                    }
                }
            }
        }
    }

    override fun onInterrupt() {
        Log.w(TAG, "Serviço interrompido")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val jsonData = it.getStringExtra("meal_json")
            jsonData?.let { json ->
                try {
                    currentMealData = JSONObject(json)
                    currentItemIndex = 0
                    Log.i(TAG, "Dados de refeição recebidos: $json")
                    
                    // Abre o Tecnonutri
                    openTecnonutri()
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao processar JSON: ${e.message}", e)
                }
            }
        }
        return START_STICKY
    }

    private fun openTecnonutri() {
        try {
            val launchIntent = packageManager.getLaunchIntentForPackage(TECNONUTRI_PACKAGE)
            launchIntent?.let {
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(it)
                Log.i(TAG, "Abrindo Tecnonutri...")
                
                // Aguarda o app abrir e depois processa
                handler.postDelayed({
                    processMealDataIfAvailable()
                }, 3000)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao abrir Tecnonutri: ${e.message}", e)
        }
    }

    private fun processMealDataIfAvailable() {
        currentMealData?.let { mealData ->
            try {
                val items = mealData.getJSONArray("itens")
                if (currentItemIndex < items.length()) {
                    processNextItem(items)
                } else {
                    Log.i(TAG, "Todos os itens processados!")
                    currentMealData = null
                    currentItemIndex = 0
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao processar dados da refeição: ${e.message}", e)
            }
        }
    }

    private fun processNextItem(items: JSONArray) {
        try {
            val item = items.getJSONObject(currentItemIndex)
            val alimento = item.getString("alimento")
            val quantidade = item.getDouble("quantidade")
            val unidade = item.optString("unidade", "g")
            
            Log.i(TAG, "Processando item ${currentItemIndex + 1}/${items.length()}: $alimento ($quantidade$unidade)")
            
            // Clica no botão de adicionar
            handler.postDelayed({
                clickFabButton()
                
                // Aguarda tela de busca
                handler.postDelayed({
                    searchFood(alimento)
                    
                    // Aguarda resultados
                    handler.postDelayed({
                        selectFirstResult()
                        
                        // Aguarda tela de quantidade
                        handler.postDelayed({
                            fillQuantity(quantidade.toString())
                            
                            // Salva
                            handler.postDelayed({
                                saveItem()
                                
                                // Próximo item
                                currentItemIndex++
                                handler.postDelayed({
                                    processMealDataIfAvailable()
                                }, 1500)
                            }, 1000)
                        }, 1000)
                    }, 2000)
                }, 1000)
            }, 500)
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao processar item: ${e.message}", e)
            currentItemIndex++
            processMealDataIfAvailable()
        }
    }

    private fun clickFabButton() {
        val rootNode = rootInActiveWindow ?: return
        val fabNode = findNodeById(rootNode, ID_FAB_ADD)
        fabNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Log.d(TAG, "Clicou no botão adicionar")
    }

    private fun searchFood(foodName: String) {
        val rootNode = rootInActiveWindow ?: return
        val searchNode = findNodeByClassName(rootNode, ID_SEARCH_FIELD)
        searchNode?.let {
            it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            val arguments = android.os.Bundle()
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, foodName)
            it.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            Log.d(TAG, "Buscando alimento: $foodName")
        }
    }

    private fun selectFirstResult() {
        val rootNode = rootInActiveWindow ?: return
        val firstResult = findClickableTextView(rootNode)
        firstResult?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Log.d(TAG, "Selecionou primeiro resultado")
    }

    private fun fillQuantity(quantity: String) {
        val rootNode = rootInActiveWindow ?: return
        val quantityNode = findNodeById(rootNode, ID_QUANTITY_FIELD)
        quantityNode?.let {
            it.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            val arguments = android.os.Bundle()
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, quantity)
            it.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            Log.d(TAG, "Preencheu quantidade: $quantity")
        }
    }

    private fun saveItem() {
        val rootNode = rootInActiveWindow ?: return
        val saveNode = findNodeById(rootNode, ID_SAVE_BUTTON)
        saveNode?.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        Log.d(TAG, "Salvou item")
    }

    private fun findNodeById(root: AccessibilityNodeInfo, resourceId: String): AccessibilityNodeInfo? {
        if (root.viewIdResourceName == resourceId) {
            return root
        }
        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { child ->
                findNodeById(child, resourceId)?.let { return it }
            }
        }
        return null
    }

    private fun findNodeByClassName(root: AccessibilityNodeInfo, className: String): AccessibilityNodeInfo? {
        if (root.className?.toString() == className) {
            return root
        }
        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { child ->
                findNodeByClassName(child, className)?.let { return it }
            }
        }
        return null
    }

    private fun findClickableTextView(root: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        if (root.className?.toString() == "android.widget.TextView" && root.isClickable) {
            return root
        }
        for (i in 0 until root.childCount) {
            root.getChild(i)?.let { child ->
                findClickableTextView(child)?.let { return it }
            }
        }
        return null
    }
}
