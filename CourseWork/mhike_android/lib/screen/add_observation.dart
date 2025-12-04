import 'package:flutter/material.dart';
import '../db/database_helper.dart';

class AddObservationScreen extends StatefulWidget {
  final int hikeId;
  const AddObservationScreen({super.key, required this.hikeId});

  @override
  State<AddObservationScreen> createState() => _AddObservationScreenState();
}

class _AddObservationScreenState extends State<AddObservationScreen> {
  final _form = GlobalKey<FormState>();
  final _obs = TextEditingController();
  final _time = TextEditingController();
  final _comment = TextEditingController();

  @override
  void initState() {
    super.initState();
    _time.text = DateTime.now().toIso8601String();
  }

  @override
  void dispose() {
    _obs.dispose();
    _time.dispose();
    _comment.dispose();
    super.dispose();
  }

  Future<void> _save() async {
    if (!_form.currentState!.validate()) return;
    await DatabaseHelper.instance.insert('observations', {
      'hikeId': widget.hikeId,
      'observation': _obs.text.trim(),
      'time': _time.text.trim(),
      'comment': _comment.text.trim().isEmpty ? null : _comment.text.trim(),
    });
    if (!mounted) return;
    Navigator.pop(context);
  }

  InputDecoration _dec(String l) => InputDecoration(labelText: l, border: const OutlineInputBorder());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Add Observation')),
      body: Padding(
        padding: const EdgeInsets.all(12),
        child: Form(
          key: _form,
          child: ListView(
            children: [
              TextFormField(controller: _obs, decoration: _dec('Observation'), validator: (v)=> (v==null||v.trim().isEmpty)?'Required':null),
              const SizedBox(height: 10),
              TextFormField(controller: _time, decoration: _dec('Time (ISO)'), validator: (v)=> (v==null||v.trim().isEmpty)?'Required':null),
              const SizedBox(height: 10),
              TextFormField(controller: _comment, decoration: _dec('Comment (optional)')),
              const SizedBox(height: 16),
              ElevatedButton.icon(onPressed: _save, icon: const Icon(Icons.save), label: const Text('Save')),
            ],
          ),
        ),
      ),
    );
  }
}
